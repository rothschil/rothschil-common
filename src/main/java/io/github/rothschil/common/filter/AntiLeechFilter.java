package io.github.rothschil.common.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AntiLeechFilter implements Filter {

    private List<String> allowedDomains;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 从配置文件中获取允许的域名列表
        String allowedDomainsStr = filterConfig.getInitParameter("allowedDomains");
        allowedDomains = Arrays.asList(allowedDomainsStr.split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String referer = request.getHeader("Referer");
        if (referer == null) {
            // 没有Referer，可能是直接在浏览器地址栏输入，也视为非法请求
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }

        boolean isValidReferer = false;
        for (String domain : allowedDomains) {
            if (referer.startsWith(domain)) {
                isValidReferer = true;
                break;
            }
        }

        if (!isValidReferer) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // 清理资源
    }
}
