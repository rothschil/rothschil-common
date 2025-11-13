package io.github.rothschil.common.config;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;

import java.util.Map;

public class MessageRedissonCacheManager extends RedissonSpringCacheManager {

    public MessageRedissonCacheManager(RedissonClient redisson) {
        super(redisson);
    }

    public MessageRedissonCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config) {
        super(redisson, config);
    }

    public MessageRedissonCacheManager(RedissonClient redisson, Map<String, ? extends CacheConfig> config, Codec codec) {
        super(redisson, config, codec);
    }

    public MessageRedissonCacheManager(RedissonClient redisson, String configLocation) {
        super(redisson, configLocation);
    }

    public MessageRedissonCacheManager(RedissonClient redisson, String configLocation, Codec codec) {
        super(redisson, configLocation, codec);
    }

    @Override
    public CacheConfig createDefaultConfig() {
        return defaultConfig;
    }


    @Getter
    @Setter
    /**
     * 默认时间   最长时间24小时  ,最大空间时间1小时
     */
    private CacheConfig defaultConfig = new CacheConfig(24 * 60 * 60 * 1000, 60 * 60 * 1000);
}
