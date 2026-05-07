package io.github.rothschil.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.rothschil.common.cache.PlusSpringCacheManager;
import io.github.rothschil.common.config.cache.caffeine.CaffeineEntry;
import io.github.rothschil.common.config.cache.caffeine.CaffeineExpiry;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * //todo 添加类描述
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@EnableCaching
@Configuration
public class CacheConfiguration {


//    /** 测试阶段，使用初始化值偏小
//     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
//     * @return com.github.benmanes.caffeine.cache.Cache<java.lang.String,java.lang.Object>
//     **/
//    @Bean
//    public Cache<String, Object> caffeineCache() {
//        return Caffeine.newBuilder().expireAfterWrite(3600, TimeUnit.SECONDS).initialCapacity(3).maximumSize(10).build();
//    }
    @Bean
    public Cache<String, CaffeineEntry> caffeineCache() {
        return Caffeine.newBuilder().expireAfter(new CaffeineExpiry()).
                initialCapacity(400).maximumSize(500000).build();
    }



    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }

}