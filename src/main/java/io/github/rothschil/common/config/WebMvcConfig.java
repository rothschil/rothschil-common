package io.github.rothschil.common.config;


import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import io.github.rothschil.common.interceptor.LogInterceptor;
import io.github.rothschil.common.response.interceptor.RequestHeaderContextInterceptorAdapter;
import io.github.rothschil.common.response.interceptor.ResponseBodyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *  <li>将系统自带的JSON序列化改为 FastJson</li>
 *  <li>将统一处理响应消息拦截器加入到应用的上下文中</li>
 * </ul>
 * @author <a href="https://github.com/rothschil">Sam</a>
*/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private ResponseBodyInterceptor responseBodyInterceptor;

    private RequestHeaderContextInterceptorAdapter requestHeaderContextInterceptorAdapter;

    @Autowired
    public void setResponseBodyInterceptor(ResponseBodyInterceptor responseBodyInterceptor) {
        this.responseBodyInterceptor = responseBodyInterceptor;
    }

    @Autowired
    public void setRequestHeaderContextInterceptor(RequestHeaderContextInterceptorAdapter requestHeaderContextInterceptorAdapter) {
        this.requestHeaderContextInterceptorAdapter = requestHeaderContextInterceptorAdapter;
    }

    /** 自定义消息转换器
     * @author <a href="https://github.com/rothschil">Sam</a>
     * @param converters 转换器
     **/
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        // 清除默认 Json 转换器
        converters.removeIf(converter -> converter instanceof MappingJackson2HttpMessageConverter);
        // 配置 FastJson
        FastJsonConfig config = new FastJsonConfig();
        config.setSerializerFeatures(SerializerFeature.PrettyFormat,SerializerFeature.QuoteFieldNames, SerializerFeature.WriteEnumUsingToString,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteDateUseDateFormat,
                SerializerFeature.DisableCircularReferenceDetect);
        config.setCharset(StandardCharsets.UTF_8);

        // 添加 FastJsonHttpMessageConverter
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        converter.setFastJsonConfig(config);
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(fastMediaTypes);
        converters.add(converter);
        // 添加 StringHttpMessageConverter，解决中文乱码问题
        StringHttpMessageConverter messageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(messageConverter);
        converters.add(0, new FastJsonHttpMessageConverter());
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        converters.add(fastConverter);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor());
        registry.addInterceptor(responseBodyInterceptor).addPathPatterns("/**").excludePathPatterns("/webjars/**", "/static/**","/mock/test/**","/iserv/**","/cache/**");
        registry.addInterceptor(requestHeaderContextInterceptorAdapter).addPathPatterns("/**").excludePathPatterns("/webjars/**", "/static/**");
    }


    @Bean
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/webjars/**") .addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/favicon.ico");
    }


}
