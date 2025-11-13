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
//    @Bean
//    public OpenAPI customOpenAPI() {
//        Contact con = new Contact();
//        return new OpenAPI()
//                .info(new Info()
//                        .title("API 文档")
//                        .description("SpringBoot3 集成 Knife4j 示例")
//                        .version("1.0.0")
//                        .contact(con));
//    }


    @Bean
    public GroupedOpenApi api4() {
        return GroupedOpenApi.builder()
                .group("all")
                .displayName("所有接口")
                .packagesToScan("io.github.rothschil")
                // 自定义全局响应码
//                .addOpenApiCustomizer((this::setCustomStatusCode))
                .build();
    }

//    private Info apiInfo() {
//        return new ApiInfoBuilder()
//                .description("Rothschil接口测试文档")
//                .contact(new Contact("WONGS", "https://blog.csdn.net/rothchil", "WCNGS@QQ.COM"))
//                .version("v1.0")
//                .title("API测试文档")
//                .build();
//    }

}
