package io.github.rothschil.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@Configuration
public class Knife4jConfig {


    @Bean
    public GroupedOpenApi api4() {
        return GroupedOpenApi.builder()
                .group("all")
                .displayName("所有接口")
                .packagesToScan("io.github.rothschil")
                // 自定义全局响应码
                .build();
    }

}
