package io.github.rothschil.common.utils;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public class UserTransmittableUtils {


    private static final TransmittableThreadLocal<Object> THREAD_LOCAL = new TransmittableThreadLocal<>();

    public static Object get() {
        return THREAD_LOCAL.get();
    }

    public static void set(Object t) {
        THREAD_LOCAL.set(t);
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
