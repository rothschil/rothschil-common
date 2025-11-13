package io.github.rothschil.common.base.dto;

/**
 * 借鉴 Python 元组设计，同时返回多个值，此处定义两个
 *
 * @author <a href="https://github.com/rothschil">Sam</a>
 * @date 2018/4/21 - 17:19
 * @since 1.0.0
 */
public class AmazTuple<P, T, R> {

    /**
     * 第一个实例
     */
    public final P fp;

    /**
     * 第二个实例
     */
    public final T st;

    /**
     * 第3个实例
     */
    public final R tt;


    public AmazTuple(P fp, T st, R tt) {
        this.fp = fp;
        this.st = st;
        this.tt = tt;
    }
}
