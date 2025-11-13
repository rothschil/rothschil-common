package io.github.rothschil.common.handler.custom;

/**
 * 自定义函数接口
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public interface CustomFunction {

    /**
     * 自定义函数名
     *
     * @return 自定义函数名
     */
    String functionName();

    /**
     * 最终执行的方法
     *
     * @param param 参数
     * @return 执行结果
     */
    String apply(Object param);
}
