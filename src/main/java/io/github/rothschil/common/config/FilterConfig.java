package io.github.rothschil.common.config;

import io.github.rothschil.common.filter.AntiLeechFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<AntiLeechFilter> antiLeechFilterRegistrationBean() {
        FilterRegistrationBean<AntiLeechFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AntiLeechFilter());
        registrationBean.addUrlPatterns("/resources/*"); // 对需要保护的资源路径进行过滤
        registrationBean.addInitParameter("allowedDomains", "http://yourdomain.com,https://yourdomain.com");
        return registrationBean;
    }

}
