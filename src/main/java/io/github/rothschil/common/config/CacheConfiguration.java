package io.github.rothschil.common.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.rothschil.common.cache.PlusSpringCacheManager;
import io.github.rothschil.common.config.cache.caffeine.CaffeineEntry;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * //todo 添加类描述
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
@EnableCaching
@Configuration
public class CacheConfiguration {


    @Value("${cache.multi.caffeine.max-size}")
    private long maxSize;

    @Value("${cache.multi.caffeine.init-size}")
    private int initSize;

    @Value("${cache.multi.caffeine.expire-seconds}")
    private long expireSec;

    @Value("${cache.multi.bloom.name}")
    private String bloomName;

    @Value("${cache.multi.bloom.expectedInsertions}")
    private long expectedInsertions;

    @Value("${cache.multi.bloom.falseProbability}")
    private double falseProbability;

    @Autowired
    protected RedissonClient redissonClient;

    private RBloomFilter<String> bloomFilter;

    private static final String NULL_PLACEHOLDER = "NULL_VALUE";

    private final Random random = new Random();




//    @Bean
//    public Cache<String, Object> caffeineCache() {
//        return Caffeine.newBuilder().expireAfterWrite(3600, TimeUnit.SECONDS).initialCapacity(3).maximumSize(10).build();
//    }

    /** 测试阶段，使用初始化值偏小
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @return com.github.benmanes.caffeine.cache.Cache<java.lang.String,java.lang.Object>
     **/
    @Bean
    public Cache<String, CaffeineEntry> caffeineCache() {
        return Caffeine.newBuilder()
                .initialCapacity(initSize)
                .maximumSize(maxSize)
                // 设置过期时间
                .expireAfterWrite(expireSec, TimeUnit.SECONDS)
                // 统计缓存命中率
                .recordStats()
                .build();
    }


    /**
     * 初始化布隆过滤器，不存在则创建
     */
    @Bean
    public RBloomFilter<String> bloomFilter() {
        bloomFilter = redissonClient.getBloomFilter(bloomName);
        if (!bloomFilter.isExists()) {
            // tryInit(预期插入数量, 误判率)，已初始化过的过滤器不会重复初始化
            bloomFilter.tryInit(expectedInsertions, falseProbability);
        }
        return bloomFilter;
    }


    /**
     * 自定义缓存管理器 整合spring-cache
     */
    @Bean
    public CacheManager cacheManager() {
        return new PlusSpringCacheManager();
    }

}