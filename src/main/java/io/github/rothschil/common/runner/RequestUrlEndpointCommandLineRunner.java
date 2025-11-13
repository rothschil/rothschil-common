package io.github.rothschil.common.runner;

import io.github.rothschil.common.utils.ApplicationContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

@Slf4j
@Component
public class RequestUrlEndpointCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("-----------------开始采集项目所有的http接口信息------------------------");

        RequestMappingHandlerMapping handlerMapping = ApplicationContextUtils.getBean("requestMappingHandlerMapping",RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        List<Map<String, String>> list = new ArrayList<>();
        handlerMethods.forEach((requestMappingInfo , handlerMethod) -> {
            Map<String, String> map = new HashMap<>();
            PatternsRequestCondition condition = requestMappingInfo.getPatternsCondition();
            if (condition != null) {
                Set<String> patterns = condition.getPatterns();
                patterns.forEach(pattern -> { map.put("url", pattern);});
            }
            map.put("className",handlerMethod.getMethod().getDeclaringClass().getName());
            map.put("methodName",handlerMethod.getMethod().getName());
            RequestMethodsRequestCondition methodsCondition = requestMappingInfo.getMethodsCondition();
            for (RequestMethod requestMethod : methodsCondition.getMethods()) {
                map.put("type", requestMethod.toString());
            }
            list.add(map);
        });
        list.forEach(m -> {
            log.info("className = {} methodName = {} request method = {} url = {} ", m.get("className"), m.get("methodName"),m.get("type"), m.get("url"));
        });
        log.info("-----------------采集项目所有的http接口信息------------------------");
    }
}
