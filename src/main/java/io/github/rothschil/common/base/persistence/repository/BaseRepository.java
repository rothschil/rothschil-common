package io.github.rothschil.common.base.persistence.repository;

import io.github.rothschil.common.base.persistence.entity.AbstractEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
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
     *
     * @param ids
     */
    void delete(ID[] ids);

    /**
     *
     * @param sql
     * @return
     */
    List<Object[]> listBySQL(String sql);

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

    /**
     *
     * @param sql
     * @param args
     */
    @Transactional(rollbackFor = Exception.class)
    void updateBySql(String sql,Object...args);


    @Transactional(rollbackFor = Exception.class)
    void updateByHql(String hql,Object...args);


    List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr);

    List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr) ;

    List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr);

    List<T> findByConditions(Map<String, String> tableMap);


    void deleteValid(String ids);

    T findOneByAttr(String attr, String condition);


    List<T> findByAttr(String attr, String condition);


    List<T> findByAttrs(String attr, String conditions);


}
