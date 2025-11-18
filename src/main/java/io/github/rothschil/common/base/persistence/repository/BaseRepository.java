package io.github.rothschil.common.base.persistence.repository;

import io.github.rothschil.common.base.persistence.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>抽象DAO层基类 提供一些简便方法<br/>
 * 想要使用该接口需要在spring配置文件的jpa:repositories中添加
 * <p/>
 * <p>泛型 ： M 表示实体类型；ID表示主键类型
 * @author WCNGS
 *
 * @param <T>
 * @param <ID>
 */
@NoRepositoryBean
@Transactional(readOnly=true,rollbackFor = Exception.class)
public interface BaseRepository<T extends AbstractEntity, ID extends Serializable> extends JpaRepository<T, ID> {

    /**
     * 根据主键删除
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param ids 主键列表
     **/
    void delete(ID[] ids);


    /**
     * 根据列的内容查找
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param clazz 类
     * @param filed 列名
     * @param value 值
     * @return java.util.List<T>
     **/
    List<T> findByField(Class<T> clazz, String filed, Object value );


    /**
     * 根据列的内容查找
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param clazz 类
     * @param map 列名-值 键值对
     * @return java.util.List<T>
     **/
    List<T> findByMultipleFiled(Class<T> clazz, LinkedHashMap<String,Object> map);

    /**
     * 根究查询条件获取数量
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param clazz 类
     * @param map       列名-值 键值对
     * @return java.util.List<T>
     **/
    int findCount(Class<T> clazz, LinkedHashMap<String, Object> map);


    /**
     * 根究查询条件获取匹配数据列表
     * 参考
     * </br>
     * <span>
     *     Params params = Params.builder().Euqal("areacode","0551").Like("phoneprefix","%551%").OrderBy("id","desc").build();</br>
     *     tblCdmaHlrRepository.findByParams(TblCdmaHlr.class,params);
     * </span>
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param clazz 类
     * @param params    参数实例
     * @return java.util.List<T>
     **/
    List<T> findByParams(Class<T> clazz, Params params);

    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param sql   SQL语句
     * @return java.util.List<java.lang.Object[]>
     **/
    List<Object[]> listBySQL(String sql);


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param sql   SQL语句
     * @param args  参数列表
     **/
    @Transactional(rollbackFor = Exception.class)
    void updateBySql(String sql,Object...args);


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param hql   SQL语句
     * @param args  参数列表
     **/
    @Transactional(rollbackFor = Exception.class)
    void updateByHql(String hql,Object...args);


    /**
     * 分页查询
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param joinField   外键关联查询，可为空
     * @param sortAttr    排序，可为空
     * @return Page
     */
    Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr);

    Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr);

    Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr);

    Page<T> findByPage(Map<String, String> tableMap);

    /**
     * 分页条件查询
     *
     * @param objConditions   查询条件
     * @param current         当前页条件
     * @param pageSize        每页条数
     * @param excludeLikeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param sortAttr        排序，可为空
     * @return Page
     */
    Page<T> findByPage(Map<String, String> objConditions, Integer current, Integer pageSize, List<String> excludeLikeAttr, String sortAttr);



    List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr);

    List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr) ;

    List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr);

    List<T> findByConditions(Map<String, String> tableMap);


    void deleteValid(String ids);

    T findOneByAttr(String attr, String condition);


    List<T> findByAttr(String attr, String condition);


    List<T> findByAttrs(String attr, String conditions);

}
