package io.github.rothschil.common.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import cn.hutool.core.util.StrUtil;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: wanger
 * @Date: 2023/12/27 11:41
 * @Description: 具体构造jpa查询的工具，本工具类只对具体的操作进行封装处理<br/>
 * 扩展需遵循规范：<br/>
 * 1.方法前面的入参时必须这四个参数predicates,root,qu,cb必须在前，后面才是自己需要的参数，通常为两种模式【数据库字段，操作值】，【数据库字段】<br/>
 * 2.当前方法功能为【数据库字段 操作符 操作值】模式时，<br/>
 * 2.1.方法名将标识操作符，<br/>
 * 2.2.第一个入参为数据库字段，且为字符串类型<br/>
 * 2.3.第二个入参为操作值，并且该操作值需要加入必要的泛型约束其类型为操作值对应的类型，如equal看为任意类型，like为字符串类型，lessThanOrEqualTo为可比较的类型<br/>
 * 3.方法内部需合法地调用jpa的api<br/>
 * 4.方法内部需进行必要的空指针，空值判定<br/>
 * 5.<span style="color:red">禁止</span>在方法内写定制化代码，如：【<br/>
 * <pre>{@code
 *  //定制代码
 *  if ("userId".equals(field)) {
 *      //定制逻辑
 *  }
 * }</pre>
 * 】<br/>
 */
public class JpaConditionUtil {
    /**
     * 排序操作【order by 字段1 排序类型,字段2 排序类型,...】
     *
     * @param root   r
     * @param qu     q
     * @param cb     c
     * @param orders 排序字段列表
     * @param <T>    操作对象泛型类型
     */
    @SafeVarargs
    public static <T> void order(Root<T> root, CriteriaQuery<?> qu, CriteriaBuilder cb, Order<T>... orders) {
        for (Order<T> order : orders) {
            if (Order.ASC.equals(order.getSort())) {
                JpaConditionUtil.orderAsc(root, qu, cb, LambdaUtil.getFieldName(order.getFieldFun()));
            } else if (Order.DESC.equals(order.getSort())) {
                JpaConditionUtil.orderDesc(root, qu, cb, LambdaUtil.getFieldName(order.getFieldFun()));
            }
        }
    }

    /**
     * 顺序排序order by 字段 asc
     *
     * @param root  r
     * @param qu    q
     * @param cb    c
     * @param field 数据库字段名称
     * @param <T>   操作对象泛型类型
     */
    private static <T> void orderAsc(Root<T> root, CriteriaQuery<?> qu, CriteriaBuilder cb, String field) {
        qu.orderBy(cb.asc(root.get(field)));
    }

    /**
     * 逆序排序【order by 字段 desc】
     *
     * @param root  r
     * @param qu    q
     * @param cb    c
     * @param field 数据库字段名称
     * @param <T>   操作对象泛型类型
     */
    private static <T> void orderDesc(Root<T> root, CriteriaQuery<?> qu, CriteriaBuilder cb, String field) {
        qu.orderBy(cb.desc(root.get(field)));
    }

    /**
     * 不等于判定【字段 != 值】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     */
    public static <T> void notEqual(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Object value) {
        if (value != null) {
            if (value instanceof String) {
                if (StrUtil.isNotBlank(value.toString())) {
                    predicates.add(cb.notEqual(root.get(field), value));
                }
            } else {
                predicates.add(cb.notEqual(root.get(field), value));
            }
        }
    }

    /**
     * 等于判定【字段 = 值】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     */
    public static <T> void equal(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Object value) {
        if (value != null) {
            if (value instanceof String) {
                if (StrUtil.isNotBlank(value.toString())) {
                    predicates.add(cb.equal(root.get(field), value));
                }
            } else {
                predicates.add(cb.equal(root.get(field), value));
            }
        }
    }

    /**
     * 小于等于判定【字段 <= 值】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void lessThanOrEqualTo(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y value) {
        if (value != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get(field), value));
        }
    }

    /**
     * 小于判定【字段 < 值】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void lessThan(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y value) {
        if (value != null) {
            predicates.add(cb.lessThan(root.get(field), value));
        }
    }

    /**
     * 大于等于判定【字段 >= 值】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void greaterThanOrEqualTo(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y value) {
        if (value != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get(field), value));
        }
    }

    /**
     * 大于判定【字段 > 值】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void greaterThan(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y value) {
        if (value != null) {
            predicates.add(cb.greaterThan(root.get(field), value));
        }
    }

    /**
     * 区间判定，不包含左右【字段 between startValue and endValue】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param startValue 起始值
     * @param endValue   结束值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void between(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y startValue, Y endValue) {
        if (startValue != null && endValue != null) {
            predicates.add(cb.between(root.get(field), startValue, endValue));
        }
    }

    /**
     * 左右闭区间判定【字段 >= startValue and 字段 <= endValue】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param startValue 起始值
     * @param endValue   结束值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void closedInterval(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y startValue, Y endValue) {
        if (startValue != null && endValue != null) {
            greaterThanOrEqualTo(predicates, root, cb, field, startValue);
            lessThanOrEqualTo(predicates, root, cb, field, endValue);
        }
    }

    /**
     * 左开右闭区间判定【字段 > startValue and 字段 <= endValue】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param startValue 起始值
     * @param endValue   结束值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void openLeftAndCloseRight(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y startValue, Y endValue) {
        if (startValue != null && endValue != null) {
            greaterThan(predicates, root, cb, field, startValue);
            lessThanOrEqualTo(predicates, root, cb, field, endValue);
        }
    }

    /**
     * 左闭右开区间判定【字段 >= startValue and 字段 < endValue】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param startValue 起始值
     * @param endValue   结束值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Comparable<? super Y>> void closeLeftAndOpenRight(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y startValue, Y endValue) {
        if (startValue != null && endValue != null) {
            greaterThanOrEqualTo(predicates, root, cb, field, startValue);
            lessThan(predicates, root, cb, field, endValue);
        }
    }

    /**
     * 包含判定【字段 in(值1,值2,...)】
     *
     * @param predicates p
     * @param root       r
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Collection<?>> void in(List<Predicate> predicates, Root<T> root, String field, Y value) {
        if (CollUtil.isNotEmpty(value)) {
            //创建一个新的list防止入参为不可修改内容对象
            List<Object> list = new ArrayList<>(value).stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(list)) {
                predicates.add(root.get(field).in(list));
            }
        }
    }

    /**
     * 包含判定【字段 not in(值1,值2,...)】
     *
     * @param predicates p
     * @param root       r
     * @param field      数据库字段名称
     * @param value      操作值
     * @param <T>        操作对象泛型类型
     * @param <Y>        操作值类型泛型约束
     */
    public static <T, Y extends Collection<?>> void notIn(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, Y value) {
        if (CollUtil.isNotEmpty(value)) {
            //创建一个新的list防止入参为不可修改内容对象
            List<Object> list = new ArrayList<>(value).stream().filter(Objects::nonNull).collect(Collectors.toList());
            if (CollUtil.isNotEmpty(list)) {
                predicates.add(cb.not(root.get(field).in(list)));
            }
        }
    }

    /**
     * 为空判定【字段 is null】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param <T>        操作对象泛型类型
     */
    public static <T> void isNull(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field) {
        if (StrUtil.isNotBlank(field)) {
            predicates.add(cb.isNull(root.get(field)));
        }
    }

    /**
     * 不为空判定【字段 is not null】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param <T>        操作对象泛型类型
     */
    public static <T> void isNotNull(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field) {
        if (StrUtil.isNotBlank(field)) {
            predicates.add(cb.isNotNull(root.get(field)));
        }
    }

    /**
     * 模糊匹配判定【字段 like ''】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param left       是否左匹配
     * @param right      是否右匹配
     * @param <T>        操作对象泛型类型
     */
    public static <T> void like(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, String value, boolean left, boolean right) {
        if (StrUtil.isNotBlank(value)) {
            predicates.add(cb.like(root.get(field), (left ? "%" : "") + value + (right ? "%" : "")));
        }
    }

    /**
     * 反向模糊匹配判定【字段 not like ''】
     *
     * @param predicates p
     * @param root       r
     * @param cb         c
     * @param field      数据库字段名称
     * @param value      操作值
     * @param left       是否左匹配
     * @param right      是否右匹配
     * @param <T>        操作对象泛型类型
     */
    public static <T> void notLike(List<Predicate> predicates, Root<T> root, CriteriaBuilder cb, String field, String value, boolean left, boolean right) {
        if (StrUtil.isNotBlank(value)) {
            predicates.add(cb.notLike(root.get(field), (left ? "%" : "") + value + (right ? "%" : "")));
        }
    }

}