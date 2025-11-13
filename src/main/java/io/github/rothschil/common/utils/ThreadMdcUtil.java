package io.github.rothschil.common.utils;

import io.github.rothschil.common.constant.Constant;
import org.slf4j.MDC;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public final class ThreadMdcUtil {
//    private static final String TRACE_ID = "traceId";

    // 获取唯一性标识
    public static String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    public static void setTraceIdIfAbsent() {
        if (MDC.get(Constant.TRACE_ID) == null) {
            MDC.put(Constant.TRACE_ID, generateTraceId());
        }
    }

    /** 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param callable
     * @param context
     * @return Callable<T>
     **/
    public static <T> Callable<T> wrap(final Callable<T> callable, final Map<String, String> context) {

        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                return callable.call();
            } finally {
                MDC.clear();
            }
        };
    }

    /**
     * 用于父线程向线程池中提交任务时，将自身MDC中的数据复制给子线程
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param runnable
     * @param context
     * @return Runnable
     **/
    public static Runnable wrap(final Runnable runnable, final Map<String, String> context) {
        return () -> {
            if (context == null) {
                MDC.clear();
            } else {
                MDC.setContextMap(context);
            }
            setTraceIdIfAbsent();
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
