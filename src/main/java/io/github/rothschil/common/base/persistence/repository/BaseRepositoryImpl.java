package io.github.rothschil.common.base.persistence.repository;

import io.github.rothschil.common.base.persistence.entity.AbstractEntity;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.utils.ReflectUtil;
import io.github.rothschil.common.utils.SortUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Id;
import jakarta.persistence.Query;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

@Slf4j
@SuppressWarnings({"unchecked"})
public class BaseRepositoryImpl<T extends AbstractEntity, ID extends Serializable> extends SimpleJpaRepository<T, ID> implements BaseRepository<T, ID> {


	private Class<T> clazz;

	//
	private final EntityManager entityManager;


	@Autowired(required = false)
	public BaseRepositoryImpl(JpaEntityInformation<T, ID> entityInformation, EntityManager em) {
		super(entityInformation, em);
		this.clazz = entityInformation.getJavaType();
		this.entityManager = em;
	}

	@Autowired(required = false)
	public BaseRepositoryImpl(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.entityManager = em;
	}


    @Override
    public List<Object[]> listBySQL(String sql) {
        return entityManager.createNativeQuery(sql).getResultList();
    }


    @Override
    public List<T> findByField(Class<T> clazz, String filed, Object value ) {
        String tablename = clazz.getName();
        List<T> list = new ArrayList<>();
        try {
            String sql="from "+tablename+" u WHERE u."+filed+"=?1";
            Query query=entityManager.createQuery(sql);
            query.setParameter(1,value);
            list= query.getResultList();
            entityManager.close();
        }catch (Exception e){
            log.error("findByField Exception {}",e.getMessage());
        }
        return list;
    }

    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @date 2025/11/14 11:30
     * @param clazz
     * @param map
     * @return java.util.List<T>
     **/
    public List<T> findByMultipleFiled(Class<T> clazz, LinkedHashMap<String,Object> map) {

        String tablename = clazz.getName();
        List<T> listRe = new ArrayList<>();
        try {
            String sql="from "+tablename+" u WHERE ";
            Set<String> set=null;
            set=map.keySet();
            List<String> list=new ArrayList<>(set);
            List<Object> filedlist=new ArrayList<>();
            int x = 1;
            for (String filed:list){
                sql+="u."+filed+"=?"+x+" and ";
                filedlist.add(filed);
                x++;
            }
            sql=sql.substring(0,sql.length()-4);
            Query query=entityManager.createQuery(sql);
            for (int i=0;i<filedlist.size();i++){
                query.setParameter(i+1,map.get(filedlist.get(i)));
            }
            listRe= query.getResultList();
            entityManager.close();
        }catch (Exception e){
            log.error("findByField Exception {}",e.getMessage());
        }
        return listRe;
    }


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @date 2025/11/14 11:30
     * @param clazz
     * @param map
     * @return java.util.List<T>
     **/
    @Override
    public int findCount(Class<T> clazz, LinkedHashMap<String, Object> map) {
        String tablename = clazz.getName();
        Query query= null;
        Object count = 0;
        try {
            String sql="select count(u) from "+tablename+" u WHERE ";
            Set<String> set=null;
            set=map.keySet();
            List<String> list=new ArrayList<>(set);
            List<Object> filedlist=new ArrayList<>();
            int x = 1;
            for (String filed:list){
                sql+="u."+filed+"=?"+ x +" and ";
                x++;
                filedlist.add(filed);
            }
            sql=sql.substring(0,sql.length()-4);
            query = entityManager.createQuery(sql);
            for (int i=0;i<filedlist.size();i++){
                query.setParameter(i+1,map.get(filedlist.get(i)));
            }
            count = query.getSingleResult();
        } catch (Exception e) {
            log.error("findByField Exception {}",e.getMessage());
        }
        return Integer.parseInt(count.toString());
    }


    @Override
    public List<T> findByParams(Class<T> clazz, Params params) {

        String tablename = clazz.getName();
        List<T> listRe = new ArrayList<>();
        //jpa运行时需要的参数
        List param = new ArrayList();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("from ").append(tablename).append(" u WHERE 1=1 ");
            int index = 0;
            //拼接sql，添加参数集合
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getAnd(), params.getAndPara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getOr(), params.getOrPara());
            index = RepositoryUtils.concatSqlForList(index,param, sql, params.getBetween(), params.getBetweenPara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getLessThan(), params.getLessThanPara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getMoreThan(), params.getMoreThanPara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getLike(), params.getLikePara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getNotLike(), params.getNotLikePara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getOrderBy(), params.getOrderByPara());
            index = RepositoryUtils.concatSqlForObj(index,param, sql, params.getNot(), params.getNotPara());

            index = RepositoryUtils.concatSqlForList(index,param, sql, params.getIn(), params.getInPara());
            index = RepositoryUtils.concatSqlForList(index,param, sql, params.getNotIn(), params.getNotInPara());

            Query query=entityManager.createQuery(sql.toString());
            //limit
            if (params.getLimitPara() != null){
                List list = params.getLimitPara().get("limit");
                query.setFirstResult((Integer) list.get(0));
                query.setMaxResults((Integer) list.get(1));
            }
            for (int i=0;i<param.size();i++){
                query.setParameter(i+1,param.get(i));
            }
            listRe= query.getResultList();
            entityManager.close();
        }catch (Exception e){
            log.error("findByField Exception {}",e.getMessage());
        }
        return listRe;
    }


    @Override
    public void delete(ID[] ids) {

    }

    @Override
    public void updateBySql(String sql, Object... args) {

        Query query = entityManager.createNativeQuery(sql);
        int i = 0;
        for (Object arg : args) {
            query.setParameter(++i, arg);
        }
        query.executeUpdate();
    }

    @Override
    public void updateByHql(String hql, Object... args) {
        Query query = entityManager.createQuery(hql);
        int i = 0;
        for (Object arg : args) {
            query.setParameter(++i, arg);
        }
        query.executeUpdate();
    }




    /**
     * 分页查询，并提供排序能力
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param joinField   外键关联查询，可为空
     * @param sortAttr    排序，可为空
     * @return Page
     */
    @Override
    public Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr) {
        int current = Integer.parseInt(tableMap.get(Constant.CURRENT));
        int pageSize = Integer.parseInt(tableMap.get(Constant.PAGE_SIZE));

        Pageable pageable;
        if (!StringUtils.isEmpty(sortAttr)) {
            pageable = PageRequest.of(current - 1, pageSize, SortUtils.sortAttr(tableMap, sortAttr));
        } else {
            pageable = PageRequest.of(current - 1, pageSize);
        }



        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, excludeAttr, joinField);
        return this.findAll(specification, pageable);
    }


    /**
     * 省去不必要的关联map
     *
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param sortAttr    排序，可为空
     * @return Page
     */
    @Override
    public Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr) {
        int current = Integer.parseInt(tableMap.get(Constant.CURRENT));
        int pageSize = Integer.parseInt(tableMap.get(Constant.PAGE_SIZE));

        Pageable pageable;
        if (!StringUtils.isEmpty(sortAttr)) {
            pageable = PageRequest.of(current - 1, pageSize, SortUtils.sortAttr(tableMap, sortAttr));
        } else {
            pageable = PageRequest.of(current - 1, pageSize);
        }

        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, excludeAttr);
        return this.findAll(specification, pageable);
    }


    /**
     * 省去map以及排序
     *
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @return Page
     */
    @Override
    public Page<T> findByPage(Map<String, String> tableMap, List<String> excludeAttr) {
        int current = Integer.parseInt(tableMap.get(Constant.CURRENT));
        int pageSize = Integer.parseInt(tableMap.get(Constant.PAGE_SIZE));
        Pageable pageable;
        pageable = PageRequest.of(current - 1, pageSize);
        //调用省去map参数的方法
        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, excludeAttr);
        return this.findAll(specification, pageable);
    }


    @Override
    public Page<T> findByPage(Map<String, String> objConditions, Integer current, Integer pageSize, List<String> excludeLikeAttr, String sortAttr) {
        Pageable pageable;
        if (!StringUtils.isEmpty(sortAttr)) {
            pageable = PageRequest.of(current - 1, pageSize, SortUtils.sortAttr(objConditions, sortAttr));
        } else {
            pageable = PageRequest.of(current - 1, pageSize);
        }

        Specification<T> specification = ReflectUtil.createSpecification(objConditions, clazz, excludeLikeAttr);
        return this.findAll(specification, pageable);
    }

    /**
     * @param tableMap 查询条件
     * @return Page
     */
    @Override
    public Page<T> findByPage(Map<String, String> tableMap) {
        int current = Integer.parseInt(tableMap.get(Constant.CURRENT));
        int pageSize = Integer.parseInt(tableMap.get(Constant.PAGE_SIZE));

        Pageable pageable;
        pageable = PageRequest.of(current - 1, pageSize);

        //调用省去map参数的方法
        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, null);
        return this.findAll(specification, pageable);
    }

    /**
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param joinField   外键关联查询，可为空
     * @param sortAttr    排序，可为空
     * @return List
     */
    @Override
    public List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, Map joinField, String sortAttr) {
        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, excludeAttr, joinField);

        if (!StringUtils.isEmpty(sortAttr)) {
            return this.findAll(specification, SortUtils.sortAttr(tableMap, sortAttr));
        } else {
            return this.findAll(specification);
        }
    }

    /**
     * 省去不必要的关联map参数
     *
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @param sortAttr    排序，可为空
     * @return List
     */
    @Override
    public List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr, String sortAttr) {
        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, excludeAttr);

        if (!StringUtils.isEmpty(sortAttr)) {
            return this.findAll(specification, SortUtils.sortAttr(tableMap, sortAttr));
        } else {
            return this.findAll(specification);
        }
    }

    /**
     * @param tableMap    查询条件
     * @param excludeAttr 是字符串类型，但是不使用模糊查询的字段，可为空
     * @return List
     */
    @Override
    public List<T> findByConditions(Map<String, String> tableMap, List<String> excludeAttr) {
        //调用省去map参数的方法
        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, excludeAttr);
        return this.findAll(specification);
    }

    /**
     * @param tableMap 查询条件
     * @return List
     */
    @Override
    public List<T> findByConditions(Map<String, String> tableMap) {
        //调用省去map参数的方法
        Specification<T> specification = ReflectUtil.createSpecification(tableMap, clazz, null);
        return this.findAll(specification);
    }


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public void deleteValid(String ids) {
        List<String> strings = Arrays.asList(ids.split(","));
        if (CollectionUtils.isEmpty(strings)) {
            return;
        }
        //获取主键
        List<Field> idAnnoation = ReflectUtil.getTargetAnnoation(clazz, Id.class);
        if (CollectionUtils.isEmpty(idAnnoation)) {
            return;
        }
        Field field = idAnnoation.get(0);
        strings.forEach(id -> {
            T object = this.findOneByAttr(field.getName(), id);
            if (object != null) {
                try {
                    ReflectUtil.setValue(object, "valid", 0);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                this.save(object);
            }
        });
    }


    @Override
    public T findOneByAttr(String attr, String condition) {
        Specification<T> specification = ReflectUtil.createOneSpecification(attr, condition);
        Optional<T> result = this.findOne(specification);

        return result.orElse(null);
    }

    @Override
    public List<T> findByAttr(String attr, String condition) {
        Specification<T> specification = ReflectUtil.createOneSpecification(attr, condition);
        return this.findAll(specification);
    }

    @Override
    public List<T> findByAttrs(String attr, String conditions) {
        List<T> results = new ArrayList<>();
        if (!StringUtils.isEmpty(conditions)) {
            List<String> cons = Arrays.asList(conditions.split(","));
            cons.forEach(condition -> {
                List<T> byAttr = findByAttr(attr, condition);
                if (byAttr != null) {
                    results.addAll(byAttr);
                }
            });
        }
        return results;
    }

}
