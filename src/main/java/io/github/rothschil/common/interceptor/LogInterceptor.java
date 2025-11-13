//package io.github.rothschil.common.interceptor;
//
//import com.alibaba.ttl.TransmittableThreadLocal;
//import com.google.common.collect.Maps;
//import io.github.rothschil.common.utils.DateUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.MDC;
//import org.springframework.lang.Nullable;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.util.Map;
//import java.util.UUID;
//
///**
// * 自定义日志拦截器，结合 logback-spring.xml 实现日志链路追踪
// * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
// * @version 1.0.0
// */
//public class LogInterceptor implements HandlerInterceptor {
//
//    private static final String TRACE_ID = "TRACE_ID";
//
//    /**
//     * 实现 TransmittableThreadLocal 的 initialValue,beforeExecute,afterExecute接口
//     */
//    static TransmittableThreadLocal<Map<String, String>> ttlMDC = new TransmittableThreadLocal() {
//        /**
//         * 在多线程数据传递的时候，将数据复制一份给MDC
//         */
//        @Override
//        protected void beforeExecute() {
//            final Map<String, String> mdc = (Map)get();
//            mdc.forEach(MDC::put);
//        }
//
//        @Override
//        protected void afterExecute() {
//            MDC.clear();
//        }
//
//        @Override
//        protected Map<String, String> initialValue() {
//            return Maps.newHashMap();
//        }
//    };
//
//
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        String tid = DateUtils.getTransId();
//        //可以考虑让客户端传入链路ID，但需保证一定的复杂度唯一性；如果没使用默认UUID自动生成
//        if (!StringUtils.isEmpty(request.getHeader("TRACE_ID"))){
//            tid=request.getHeader("TRACE_ID");
//        }
//        //同时给TransmittableThreadLocal记录traceId
//        ttlMDC.get().put("traceId", tid);
//        MDC.put(TRACE_ID, tid);
//        return true;
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
//                                @Nullable Exception ex) {
//        MDC.remove(TRACE_ID);
//        ttlMDC.get().clear();
//        ttlMDC.remove();
//    }
//}
