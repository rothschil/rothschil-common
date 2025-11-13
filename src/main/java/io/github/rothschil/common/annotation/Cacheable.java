package io.github.rothschil.common.annotation;

import io.github.rothschil.common.aspect.CacheAspect;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 缓存查询注解，详情请查看 {@link CacheAspect}<br/>
 *
 * 1、修订内容有支持三木运算，目前只能支持到二级运算，对 value 做了更进一步的支持</br>
 * 用法有三种：
 * <ul>
 * <li>a)、直接使用字符串 <b>KEY</b></li>
 * <li>b)、使用 <b>示例.属性名</b> 如：dataConfig.areacode</li>
 * <li>c)、使用 <b>{#示例.属性名}</b> 如：{#dataConfig.areacode}</li>
 * </ul><br/>
 * 2、新增 Caffeine 作为一级缓存，替代原先 Redis/TeleCache 在框架中的作用，并将Redis/TeleCache作用属性下沉为系统架构的二级缓存，
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Cacheable {

    String prefix() default "";

    @AliasFor("key") String value() default "";

    /**
     * 是否启用 caffeine 作为一级缓存
     */
    boolean enableCaffeine() default false;

    String key() default "";

    /**
     * 单位：分钟
     */
    int ttl() default -1;

}
