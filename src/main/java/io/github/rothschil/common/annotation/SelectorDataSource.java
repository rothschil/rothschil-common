package io.github.rothschil.common.annotation;

import io.github.rothschil.common.constant.DataSourceNamesConstant;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectorDataSource {

    String value() default DataSourceNamesConstant.ONE;
}
