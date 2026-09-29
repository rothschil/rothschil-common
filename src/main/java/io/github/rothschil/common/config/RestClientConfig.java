package io.github.rothschil.common.config;

import io.github.rothschil.common.handler.LogExecChainHandler;
import io.github.rothschil.web.client.ApiClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;



/**
 *
 * @author: <a href="mailto:WCNGS@QQ.COM">Sam</a>
 **/

@Configuration
public class RestClientConfig {

    @Bean
    public ApiClient goodsClient() {
        String baseUrl = "http://localhost:11200";
        CloseableHttpClient httpClient = HttpClients.custom().addExecInterceptorFirst("log", new LogExecChainHandler()).build();
        RestClient restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient) {{
                    setConnectTimeout(5000);
                    setConnectionRequestTimeout(5000);
                }})
                .build();
        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(ApiClient.class);
    }

}
