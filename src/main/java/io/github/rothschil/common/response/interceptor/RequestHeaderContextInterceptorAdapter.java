package io.github.rothschil.common.response.interceptor;

import io.github.rothschil.common.base.vo.RequestHeaderVo;
import io.github.rothschil.common.constant.Constant;
import io.github.rothschil.common.utils.IpUtil;
import io.github.rothschil.common.utils.UserTransmittableUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 对请求进行统一处理
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Component
@Slf4j
public class RequestHeaderContextInterceptorAdapter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        initHeaderContext(request);
        // 返回 true 继续执行，返回 false 中断请求
        return true;
    }

    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param request
     **/
    private void initHeaderContext(HttpServletRequest request){
        String origClientIp = IpUtil.getIpAddr(request);
        String userAgent = request.getHeader(Constant.USER_AGENT);
        RequestHeaderVo reqVo = RequestHeaderVo.builder().origClientIp(origClientIp).userAgent(userAgent).build();
        UserTransmittableUtils.set(reqVo);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        UserTransmittableUtils.clear();
    }
}
