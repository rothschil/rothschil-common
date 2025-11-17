package io.github.rothschil.common.response.interceptor;


import io.github.rothschil.common.constant.Constant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * 定义统一响应内容的拦截器，为内容进行设置 {@link Constant } RESPONSE_RESULT_ANN，可以识别和兼容 方法 和 类的 Annotation 注解
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Component
public class ResponseBodyInterceptor implements HandlerInterceptor {

    /**
     * Handler 处理 之前的预处理，增加 对 {@link RestController} 注解捕获、添加 标记
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  HandlerMethod
     * @return boolean 预处理结果为 True 才会进行后续所有操作
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) {
        Assert.notNull(request, "The request must not be null");
        Assert.notNull(response, "The response must not be null");
        String requestURI = request.getRequestURI();
        trace(request);

        if(!requestURI.startsWith("/mock/test") && !requestURI.startsWith("/iserv")) {
            if (handler instanceof HandlerMethod) {
                final HandlerMethod handlerMethod = (HandlerMethod) handler;
                final Class<?> clazz = handlerMethod.getBeanType();
                if (clazz.isAnnotationPresent(RestController.class)) {
                    request.setAttribute(Constant.RESPONSE_RESULT_ANN, clazz.getAnnotation(RestController.class));
                }
            }
        } else {
            return false;
        }
        return true;
    }

//    private final static String TRACE_ID = "traceId";

    public void trace(HttpServletRequest request) {
        //可以考虑让客户端传入链路ID，但需保证一定的复杂度唯一性；如果没使用默认UUID自动生成
        String callId = request.getHeader("callId");
        if (StringUtils.isEmpty(callId)) {
            callId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put(Constant.TRACE_ID, callId);
    }


    /**
     * 请求结束执行 只有在 {@link ResponseBodyInterceptor} 中 <b>preHandle</b> 结果为 <b>True</b>，才执行
     *
     * @param request      HttpServletRequest
     * @param response     HttpServletResponse
     * @param handler      handler
     * @param modelAndView modelAndView
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }


    /**
     * 视图渲染完成后才执行，但是需要满足条件：
     * 请求结束执行 只有在 {@link ResponseBodyInterceptor}  .preHandle 结果为 True，才执行
     *
     * @param request  HttpServletRequest
     * @param response HttpServletResponse
     * @param handler  handler
     * @param ex       ex
     * @author <a href="https://github.com/rothschil">Sam</a>
     **/
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
        MDC.remove(Constant.TRACE_ID);
    }
}
