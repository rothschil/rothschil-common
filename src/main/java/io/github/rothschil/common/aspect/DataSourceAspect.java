package io.github.rothschil.common.aspect;

import io.github.rothschil.common.annotation.SelectorDataSource;
import io.github.rothschil.common.datasource.DynamicDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 数据源AOP切面处理
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Aspect
@Component
public class DataSourceAspect implements Ordered {

    protected Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 切点: 所有配置 DataSource 注解的方法
     */
    @Pointcut("@annotation(io.github.rothschil.common.annotation.SelectorDataSource)")
    public void dataSourcePointCut() {
    }

    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        SelectorDataSource ds = method.getAnnotation(SelectorDataSource.class);
        // 通过判断 DataSource 中的值来判断当前方法应用哪个数据源
        DynamicDataSource.setDataSource(ds.value());
        log.debug("当前数据源: {}" , ds.value());
        log.debug("Set datasource is " + ds.value());
        try {
            return point.proceed();
        } finally {
            DynamicDataSource.clearDataSource();
            log.debug("clean datasource");
        }
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
