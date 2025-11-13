package io.github.rothschil.common.base.persistence.service;


import io.github.rothschil.common.base.persistence.entity.AbstractEntity;
import io.github.rothschil.common.base.persistence.repository.BaseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 * Service基类
 * @author WCNGS
 * @date 2017年4月24日
 *
 */
public abstract class BaseService<R extends JpaRepository<T,ID>, T extends AbstractEntity<?>, ID extends Serializable> {

    /**
     * 日志对象
     */
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected BaseRepository<T, ID> baseRepository;


    public BaseService() {
    }


    /**
     * 保存单个实体
     *
     * @param t 实体
     * @return 返回保存的实体
     */
    public T save(T t) {
        return baseRepository.save(t);
    }

    public T saveAndFlush(T t) {
        t = save(t);
        baseRepository.flush();
        return t;
    }


    /**
     * 根据主键删除相应实体
     *
     * @param id 主键
     */
    public void delete(ID id) {
        baseRepository.deleteById(id);
    }

    /**
     * 删除实体
     *
     * @param t 实体
     */
    public void delete(T t) {
        baseRepository.delete(t);
    }


    /**
     * 按照主键查询
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    public T findById(ID id) {
        Optional<T> optional= baseRepository.findById(id);
        return optional.orElse(null);
    }


    /**
     * 按照主键查询
     *
     * @param id 主键
     * @return 返回id对应的实体
     */
    public T findOne(ID id) {
        return findById(id);
    }


    /**
     * 实体是否存在
     * @author      WCNGS@QQ.COM
     * @param id                id 主键
     * @return      boolean   存在 返回true，否则false
     * @date        2018/7/3 22:08
     */
    public boolean exists(ID id) {
        return findOne(id) == null;
    }


    /**
     *
     * @param sql   SQL
     * @param args  数组条件参数
     **/
    public void updateBySql(String sql, Object... args){
        baseRepository.updateBySql(sql,args);
    }

    /**
     *
     * @param hql   HQL
     * @param args 数组条件参数
     **/
    public void updateByHql(String hql, Object... args){
        baseRepository.updateByHql(hql,args);
    }


    public List<Object[]> listBySQL(String sql){
        return baseRepository.listBySQL(sql);
    }

    /**
     * 条件查询分页
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param joinField   外键关联查询，可为空
     * @param sortAttr    排序，可为空
     * @return Page
     */
    public Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr){
        return baseRepository.findByPage(tableMap,excludeAttr,joinField,sortAttr);
    }

    /**
     * 省去不必要的关联map
     *
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param sortAttr    排序，可为空
     * @return Page
     */
    public Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr){
        return baseRepository.findByPage(tableMap,excludeAttr,sortAttr);
    }


    /**
     * 省去map以及排序
     *
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @return Page
     */
    public Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr){
        return baseRepository.findByPage(tableMap,excludeAttr);
    }

    /**
     * @param tableMap 查询条件
     * @return Page
     */
    public Page<T> findByPage(Map<String, String> tableMap){
        return baseRepository.findByPage(tableMap);
    }


    /**
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param joinField   外键关联查询，可为空
     * @param sortAttr    排序，可为空
     * @return List
     */
    public List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr){
        return baseRepository.findByConditions(tableMap,excludeAttr,joinField,sortAttr);
    }


    /**
     * 省去不必要的关联map参数
     *
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param sortAttr    排序，可为空
     * @return List
     */
    public List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr){
        return baseRepository.findByConditions(tableMap,excludeAttr,sortAttr);
    }

    /**
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @return List
     */
    public List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr){
        return baseRepository.findByConditions(tableMap,excludeAttr);
    }

    /**
     * @param tableMap 查询条件
     * @return List
     */
    public List<T> findByConditions(Map<String, String> tableMap){
        return baseRepository.findByConditions(tableMap);
    }

    public T findOneByAttr(String attr, String condition){
        return baseRepository.findOneByAttr(attr,condition);
    }

    public List<T> findByAttr(String attr, String condition){
        return baseRepository.findByAttr(attr,condition);
    }

    public List<T> findByAttrs(String attr, String conditions){
        return baseRepository.findByAttrs(attr,conditions);
    }

    /**
     * 统计实体总数
     * @author      WCNGS@QQ.COM
     * @return      long
     * @date        2018/7/3 22:07
     */
    public long count() {
        return baseRepository.count();
    }


    /**
     * 查询所有实体
     * @author      WCNGS@QQ.COM
     * @return      java.util.List<T>
     * @date        2018/7/3 22:07
     */
    public List<T> findAll() {
        return baseRepository.findAll();
    }

    /**
     * 按照顺序查询所有实体
     * @author      WCNGS@QQ.COM
     * @param sort  排序
     * @return      java.util.List<T>
     * @date        2018/7/3 22:06
     */
    public List<T> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }


    /**
     * 分页及排序查询实体
     *
     * @param pageable 分页及排序数据
     * @return Page<T>
     * @date        2018/7/3 22:06
     */
    public Page<T> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }

    public Page<T> findEntityNoCriteria(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return findAll(pageable);
    }


    public Page<T> findCriteria(Integer page, Integer size,final AbstractEntity<?> ae) {
        Pageable pageable = PageRequest.of(page, size);

//        Sort sort = new Sort(Sort.Direction.DESC, "id");

        return findAll(pageable);
    }


}
