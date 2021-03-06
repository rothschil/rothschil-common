package io.github.rothschil.base.elastic.service;


import com.alibaba.fastjson.JSON;
import io.github.rothschil.base.elastic.entity.BoolCondition;
import io.github.rothschil.common.constant.Constants;
import io.github.rothschil.common.po.BasePo;
import io.github.rothschil.common.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;

/**
 * 对外提供对 elasticsearch 三类主要功能，在持久化层面上，本来使用<b>elasticsearch</b>机制 与其他有不同，测试过程中发现
 * 持久化的数据与 测试单例结束 时间有 10秒的延迟，利用 {@link ElasticsearchRestTemplate} 集成 {@link NativeSearchQueryBuilder} 的复合条件的操作。
 * <ul>
 *     <li>1、<b>索引管理：索引管理主要为 创建索引、判断索引存在、删除索引</b></li>
 *     <li>2、<b>数据管理：数据的持久化</b></li>
 *     <li>3、<b>数据批量：</b></li>
 * </ul>
 * <hr/>
 * <p>
 *     补充:对单个实体类的 CRUD 建议定义接口，并继承 {@link ElasticsearchRepository} 用来完成资源 实例资源的操作，这个过程更加可控
 * </p>
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2019/08/29 - 16:24
 * @since 1.0.0
 */
@SuppressWarnings("unused")
@Slf4j
@Component
public class ElasticIndexManger extends AbstractElasticIndexManger {


    /**
     * 创建索引，默认分片数量为 1，即一个主片，副本数量为 0
     *
     * @param indexName 索引名称
     * @param mapping   索引定义，JSON形式的字符串
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:30
     */
    public void createIndex(String indexName, String mapping) {
        createIndex(indexName, mapping, 0, 1);
    }


    /**
     * 指定索引结构创建索引
     *
     * @param indexName 索引名称
     * @param mapping   索引定义，JSON形式的字符串
     * @param replicas  副本的数量
     * @param shards    分片数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:30
     */
    public void createIndex(String indexName, String mapping, int replicas, int shards) {
        try {
            if (!this.existIndex(indexName)) {
                log.error(" indexName={} 已经存在,mapping={}", indexName, mapping);
                return;
            }
            CreateIndexRequest request = new CreateIndexRequest(indexName);
            buildSetting(request, replicas, shards);
            request.mapping(mapping, XContentType.JSON);
            CreateIndexResponse res = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);
            if (!res.isAcknowledged()) {
                throw new RuntimeException("初始化失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    /**
     * 获取所有索引，默认为所有索引
     *
     * @return List
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/10/30-11:54
     **/
    public List getAllIndex() {
        return getAllIndex(Constants.MULTI_CHARACTER);
    }

    /**
     * 获取所有索引，按照正则表达式方式过滤 索引名称，并返回符合条件的索引名字
     *
     * @param inPattern 正则表达式
     * @return List
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/10/30-11:54
     **/
    public List<String> getAllIndex(String inPattern) {
        GetIndexRequest getIndexRequest = new GetIndexRequest(inPattern);
        try {
            GetIndexResponse getIndexResponse = restHighLevelClient.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            String[] indices = getIndexResponse.getIndices();
            return Arrays.asList(indices);
        } catch (IOException e) {
            log.error("获取索引失败 {} 已经存在", e.getMessage());
        } catch (ElasticsearchStatusException e) {
            log.error("获取索引失败 {} 索引本身不存在", e.getMessage());
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 制定配置项的判断索引是否存在，注意与 isExistsIndex 区别
     * <ul>
     *     <li>1、可以指定 用本地检索 还是用 主动节点方式检索</li>
     *     <li>2、是否适应被人读的方式</li>
     *     <li>3、返回默认设置</li>
     * </ul>
     *
     * @param indexName index名
     * @return boolean
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:27
     */
    public boolean existIndex(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        //TRUE-返回本地信息检索状态，FALSE-还是从主节点检索状态
        request.local(false);
        //是否适应被人可读的格式返回
        request.humanReadable(true);
        //是否为每个索引返回所有默认设置
        request.includeDefaults(false);
        //控制如何解决不可用的索引以及如何扩展通配符表达式,忽略不可用索引的索引选项，仅将通配符扩展为开放索引，并且不允许从通配符表达式解析任何索引
        request.indicesOptions(IndicesOptions.lenientExpandOpen());
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    /**
     * 单纯断某个索引是否存在
     *
     * @param indexName index名
     * @return boolean 存在为True，不存在则 False
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:27
     */
    public boolean isIndexExists(String indexName) throws Exception {
        return restHighLevelClient.indices().exists(new GetIndexRequest(indexName), RequestOptions.DEFAULT);
    }

    /**
     * 批量插入数据，通过 {@link List} 的对象集合进行插入，此处对失败的提交进行二次提交，并覆盖原有数据，这一层面是 ElasticSearch自行控制
     *
     * @param indexName index
     * @param list      列表
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:26
     */
    public void batch(String indexName, List<? extends BasePo> list) throws IOException {
        int sleep = 15;
        BulkRequest request = new BulkRequest();
        list.forEach(item -> request.add(new IndexRequest(indexName)
                .id(item.getId().toString())
                .source(JSON.toJSONString(item), XContentType.JSON)));
        try {
            BulkResponse bulkResponse = bulk(request);
            log.error("[Verification BulkResponse bulk 操作结果] {}, 文件大小 {} ", bulkResponse.status(), list.size());
            if (bulkResponse.hasFailures()) {
                log.error(bulkResponse.buildFailureMessage());
                for (BulkItemResponse bulkItemResponse : bulkResponse) {
                    if (bulkItemResponse.isFailed()) {
                        log.error(bulkItemResponse.getFailureMessage());
                    }
                }
                log.error("批量操作失败，重新再提交一次,间隔时间{}， 文件大小 {} ", sleep, list.size());
                TimeUnit.SECONDS.sleep(sleep);
                bulkResponse = bulk(request);
                if (bulkResponse.hasFailures()) {
                    log.error("再次提交失败，需要写入MQ ， 文件大小 {} ", list.size());
                }
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * bulk 方式批量提交
     *
     * @param request {@link BulkRequest} 请求
     * @return BulkResponse
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/10/24-15:50
     **/
    private BulkResponse bulk(BulkRequest request) throws IOException {
        return restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
    }

    /**
     * <p>
     * 批量插入数据，通过 {@link List} 的对象集合进行插入，提交前，判断 该索引是否存在不存在则直接创建 该索引
     * 并对失败的提交进行二次提交，并覆盖原有数据，这一层面是 ElasticSearch自行控制
     * </p>
     *
     * @param indexName index
     * @param list      列表
     * @param created   当索引不存在，则创建索引，默认为 true，即索引不存在，创建该索引，此时 mapping 应该不为空
     * @param mapping   索引定义，JSON形式的字符串
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:26
     */
    public void batch(List<? extends BasePo> list, String indexName, boolean created, String mapping) throws Exception {
        try {
            if (!isIndexExists(indexName)) {
                log.error("[Index does not exist] Rebuilding index. IndexName ={}", indexName);
                if (created && StringUtils.isNotBlank(mapping)) {
                    createIndex(indexName, mapping);
                } else {
                    log.error("[Index does not exist , No index creation] IndexName ={}", indexName);
                    return;
                }
            }
            batch(indexName, list);
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 批量删除，根据索引名称，删除索引下数据
     *
     * @param indexName index
     * @param idList    待删除列表
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:14
     */
    public <T> void deleteBatch(String indexName, Collection<T> idList) {
        BulkRequest request = new BulkRequest();
        idList.forEach(item -> request.add(new DeleteRequest(indexName, item.toString())));
        try {
            restHighLevelClient.bulk(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据索引名称，和 {@link SearchSourceBuilder} 条件，以及返回对象实体类，返回列表
     *
     * @param indexName index
     * @param builder   查询参数
     * @param clazz     结果类对象
     * @return java.util.List<T>
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:14
     */
    public <T> List<T> search(String indexName, SearchSourceBuilder builder, Class<T> clazz) {
        List res = Collections.EMPTY_LIST;
        try {
            SearchRequest request = new SearchRequest(indexName);
            request.source(builder);
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHit[] hits = response.getHits().getHits();
            res = new ArrayList<>(hits.length);
            for (SearchHit hit : hits) {
                res.add(JSON.parseObject(hit.getSourceAsString(), clazz));
            }
        } catch (IOException e) {
            log.error("[ElasticSearch] connect err ,err-msg {}", e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    /**
     * 删除 index，以及索引下数据
     *
     * @param indexName 索引名字
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:13
     */
    public void deleteIndex(String indexName) {
        try {
            restHighLevelClient.indices().delete(new DeleteIndexRequest(indexName), RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除索引下数据，但是不删除索引结构
     *
     * @param builder    条件构建模式
     * @param indexNames 索引名称列表
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2019/10/17 17:13
     */
    public void deleteByQuery(QueryBuilder builder, String... indexNames) {
        try {
            DeleteByQueryRequest request = builderDeleteRequest(builder, indexNames);
            BulkByScrollResponse response = restHighLevelClient.deleteByQuery(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 不推荐使用，原因为不够灵活，获取该索引下可以匹配的数量，支持 模糊查询和精确查询，
     * 用法 在 方法 <b>field</b> 的处理上。
     * <ul>
     *     <li>模糊匹配模式：字段</li>
     *     <li>精确匹配模式：字段.类型</li>
     * </ul>
     *
     * @param indexName 文档索引名
     * @param field     字段
     * @param text      内容
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2018/07/20-20:47
     **/
    @Deprecated
    public long countMatchPhrasePrefixQuery(String indexName, String field, String text) {
        CountRequest countRequest = new CountRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(matchPhrasePrefixQuery(field, text));
        countRequest.source(searchSourceBuilder);
        CountResponse countResponse = null;
        try {
            countResponse = restHighLevelClient.count(countRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return countResponse == null ? 0L : countResponse.getCount();
    }


    /**
     * 按照字段 内容进行精确匹配，返回匹配的数量
     *
     * @param field      字段名
     * @param content    内容
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:49
     **/
    public long exactCondition(String field, String content, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.termQuery(field, content));
        return count(builder, indexNames);
    }


    /**
     * 按照字段的前缀内容进行匹配，返回匹配的数量
     *
     * @param field      字段名
     * @param prefix     前缀
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:49
     **/
    public long prefix(String field, String prefix, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.prefixQuery(field, prefix));
        return count(builder, indexNames);
    }


    /**
     * 按照字段对 内容进行后缀匹配，返回匹配的数量
     *
     * @param suffix     后缀
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:56
     **/
    public long suffix(String field, String suffix, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.wildcardQuery(field, Constants.MULTI_CHARACTER + suffix));
        return count(builder, indexNames);
    }


    /**
     * 字段的前缀和后缀都必须满足条件
     *
     * @param field      字段
     * @param prefix     前缀
     * @param suffix     后缀
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:59
     **/
    public long prefixAndSuffix(String field, String prefix, String suffix, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.prefixQuery(field, prefix));
        builder.must(QueryBuilders.wildcardQuery(field, Constants.MULTI_CHARACTER + suffix));
        return count(builder, indexNames);
    }

    /**
     * 字段的前缀和后缀都满足一个条件按即可
     *
     * @param field      字段
     * @param prefix     前缀
     * @param suffix     后缀
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:59
     **/
    public long prefixOrSuffix(String field, String prefix, String suffix, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.prefixQuery(field, prefix));
        builder.should(QueryBuilders.wildcardQuery(field, Constants.MULTI_CHARACTER + suffix));
        return count(builder, indexNames);
    }

    /**
     * 字段的前缀必须满足，而 后缀则不要求 不一定满足
     *
     * @param field      字段
     * @param prefix     前缀
     * @param suffix     后缀
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:59
     **/
    public long prefixMustSuffixShould(String field, String prefix, String suffix, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.prefixQuery(field, prefix));
        builder.should(QueryBuilders.wildcardQuery(field, Constants.MULTI_CHARACTER + suffix));
        return count(builder, indexNames);
    }

    /**
     * 字段的前缀选择性满足，而 后缀则一定要满足
     *
     * @param field      字段
     * @param prefix     前缀
     * @param suffix     后缀
     * @param indexNames 索引名
     * @return long 数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-10:59
     **/
    public long prefixShouldSuffixMust(String field, String prefix, String suffix, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.should(QueryBuilders.prefixQuery(field, prefix));
        builder.must(QueryBuilders.wildcardQuery(field, Constants.MULTI_CHARACTER + suffix));
        return count(builder, indexNames);
    }


    /**
     * 查询总数
     *
     * @param indexNames 索引文档名称，可以是多个
     * @return long 匹配的数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/10/29-21:11
     **/
    public long total(String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        return count(builder, indexNames);
    }

    /**
     * 查询匹配条件，支持同时对多个索引进行查询，只要将索引名称按照 字符数组形式组成即可
     *
     * @param params     Map形式的 字段名 和 字段内容 组成的条件
     * @param bool      复合条件封装
     * @param indexNames 索引名，可以一次性查询多个
     * @return long 最终数量
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-9:26
     **/
    public BulkByScrollResponse update(Map<String, Object> params, BoolCondition bool, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        setBuilders(builder,bool);
        return update(params,builder,indexNames);
    }

    /**
     * 查询匹配条件，支持同时对多个索引进行查询，只要将索引名称按照 字符数组形式组成即可
     *
     * @param page    当前页
     * @param size    每页大小
     * @param clazz      Class对象
     * @param indexNames 索引名，可以一次性查询多个
     * @return SearchHits 命中结果的数据集
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @date 2021/11/1-9:26
     **/
    protected SearchHits<? extends BasePo> searchPage(int page, int size, BoolCondition bool,Class<? extends BasePo> clazz, String... indexNames) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        setBuilders(builder,bool);
        return search(page,size,builder, clazz, indexNames);
    }

}



