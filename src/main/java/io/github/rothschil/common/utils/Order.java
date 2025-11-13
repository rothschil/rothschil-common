package io.github.rothschil.common.utils;

import cn.hutool.core.lang.func.Func1;
import lombok.Getter;

/**
 * @Author: wanger
 * @Date: 2024/1/2 9:35
 * @Description: 自定义排序类调用参考示例【<pre>{@code
 *         List<UserEntity> list = userService.list(MyJpaUtil.create(UserEntity.class)
 *                 .equal(UserEntity::getStatus, DataStatus.NORMAL.getStatus())
 *                 //其他条件...
 *                 .order(Order.DESC(UserEntity::getCreatedTime), Order.ASC(UserEntity::getPhone),其他排序字段)
 *                 );
 *         }</pre>】
 */
@Getter
public class Order<T> {
    /**
     * 顺序排序标识
     */
    public static final String ASC = "ASC";
    /**
     * 倒序排序标识
     */
    public static final String DESC = "DESC";
    /**
     * 当前的排序标识
     */
    private final String sort;
    /**
     * 排序字段lambda方法，如【UserEntity::getAge】
     */
    private final Func1<T, ?> fieldFun;

    /**
     * 私有构造器，仅供内部ASC与DESC两个方法使用
     *
     * @param fieldFun 排序字段lambda方法，如【UserEntity::getAge】
     * @param sort     排序状态，ASC/DESC
     */
    private Order(Func1<T, ?> fieldFun, String sort) {
        this.fieldFun = fieldFun;
        this.sort = sort;
    }

    /**
     * 顺序排序方式，如【Order.ASC(UserEntity::getAge)】
     *
     * @param fieldFun 排序字段lambda方法，如【UserEntity::getAge】
     * @param <T>      调用者的泛型类型
     * @return 返回排序对象
     */
    public static <T> Order<T> ASC(Func1<T, ?> fieldFun) {
        return new Order<>(fieldFun, ASC);
    }

    /**
     * 顺序排序方式，如【Order.DESC(UserEntity::getAge)】
     *
     * @param fieldFun 排序字段lambda方法，如【UserEntity::getAge】
     * @param <T>      调用者的泛型类型
     * @return 返回排序对象
     */
    public static <T> Order<T> DESC(Func1<T, ?> fieldFun) {
        return new Order<>(fieldFun, DESC);
    }

}