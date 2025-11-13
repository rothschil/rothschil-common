package io.github.rothschil.common.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {


    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    //获取Bean
    public static <T> T getBean(Class<T> requiredType){
        return getApplicationContext().getBean(requiredType);
    }

    /**
     * 根据bean的id和类型获取bean对象
     * @param beanName
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(String beanName,Class<T> clazz){
        return clazz.cast(getBean(beanName));
    }

    public static <T> T getBean(String name){
        return (T) getApplicationContext().getBean(name);
    }
}
