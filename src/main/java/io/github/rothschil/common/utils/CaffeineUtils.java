package io.github.rothschil.common.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import org.checkerframework.checker.index.qual.NonNegative;
import org.springframework.lang.NonNull;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * //todo 添加类描述
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 * @version 1.0.0
 */
public class CaffeineUtils {

    protected static class CacheObject<T> {
        T data;
        long expire;

        public CacheObject(T data, long second) {
            this.data = data;
            this.expire = TimeUnit.SECONDS.toNanos(second);
        }
    }

    /**
     * 不设置过期时长,即默认时间为5分钟
     */
    public static final long NOT_EXPIRE = 5;

    public static final Cache<String, CacheObject<?>> CAFFEINE = Caffeine.newBuilder()
            .expireAfter(new Expiry<String, CacheObject<?>>() {
                @Override
                public long expireAfterCreate(@NonNull String key, @NonNull CacheObject<?> value, long currentTime) {
                    //将过期时间设置为自定义的过期时间
                    return value.expire;
                }

                @Override
                public long expireAfterUpdate(@NonNull String key, @NonNull CacheObject<?> value, long currentTime, @NonNegative long currentDuration) {
                    return value.expire;
                }

                @Override
                public long expireAfterRead(@NonNull String key, @NonNull CacheObject<?> value, long currentTime, @NonNegative long currentDuration) {
                    return value.expire;
                }
            })
            .initialCapacity(100)
            .maximumSize(1024)
            .build();


    public static <T> void set(String key, T value, long expire) {
        CacheObject<T> cacheObject = new CacheObject<>(value, expire);
        CAFFEINE.put(key, cacheObject);
    }

    public static void set(String key, Object value) {
        set(key, value, NOT_EXPIRE);
    }



    public static <T> T get(String key,Class<T> clazz) {
        CacheObject<?> cacheObject = CAFFEINE.getIfPresent(key);
        if (cacheObject==null) {
            return null;
        }
        return (T) cacheObject.data;
    }

    public static void delete(String key) {
        CAFFEINE.invalidate(key);
    }

    public static void delete(Collection<String> keys) {
        CAFFEINE.invalidateAll(keys);
    }
}
