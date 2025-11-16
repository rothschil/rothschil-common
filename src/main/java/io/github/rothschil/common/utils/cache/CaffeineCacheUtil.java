package io.github.rothschil.common.utils.cache;

import io.github.rothschil.common.config.cache.caffeine.CaffeineEntry;
import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class CaffeineCacheUtil {

    private static Cache cache = null;

    static {
        cache = SpringUtil.getBean(Cache.class);
    }

    /**
     * 加入缓存，默认过期时间24小时且读后不刷新
     *
     * @param key
     * @param value
     */
    public static void putIntoCache(String key, Object value) {
        putIntoCache(key, value, 24, TimeUnit.HOURS, false);
    }

    /**
     * 加入缓存，可指定过期时间，默认不刷新 默认是分钟
     *
     * @param key
     * @param value
     * @param duration
     */
    public static void putIntoCache(String key, Object value, long duration) {
        putIntoCache(key, value, duration, TimeUnit.MINUTES, false);
    }


    /**
     * 加入缓存，可指定过期时间、读后是否刷新
     *
     * @param key
     * @param value
     * @param duration
     * @param timeUnit
     */
    public static void putIntoCache(String key, Object value, long duration, TimeUnit timeUnit) {
        putIntoCache(key, value, duration, timeUnit, false);
    }

    /**
     * 加入缓存，可指定过期时间、读后是否刷新
     *
     * @param key
     * @param value
     * @param duration
     * @param timeUnit
     * @param accessFresh
     */
    public static void putIntoCache(String key, Object value, long duration, TimeUnit timeUnit, boolean accessFresh) {
        CaffeineEntry caffeineEntry = new CaffeineEntry();
        caffeineEntry.setKey(key);
        caffeineEntry.setValue(value);
        caffeineEntry.setExpireTime(timeUnit.toNanos(duration));
        caffeineEntry.setAccessFresh(accessFresh);
        cache.put(key, caffeineEntry);
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getCacheObject(String key, Class<T> clazz) {
        final Object result = getCacheObject(key);
        return Objects.nonNull(result) ? clazz.cast(result) : null;
    }

    /**
     * 获取缓存集合
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getCacheObjectList(String key, Class<T> clazz) {
        final Object result = getCacheObject(key);
        if (Objects.nonNull(result)) {
            Collection<?> collection = (Collection<?>) result;
            List<T> list = new ArrayList<>();
            collection.forEach(item -> list.add(clazz.cast(item)));
            return list;
        }
        return null;
    }

    /**
     * 获取基本缓存对象
     *
     * @param key
     * @return
     */
    public static Object getCacheObject(String key) {
        final Object[] value = {null};
        CaffeineEntry entry=(CaffeineEntry)cache.getIfPresent(key);
        Optional.ofNullable(entry)
                .ifPresent(item -> {
                    value[0] = item.getValue();
                });
        return value[0];
    }

    /**
     * 删除单个缓存
     * @param key
     */
    public static void deleteCacheObject(String key) {
        cache.invalidate(key); // 手动删除指定键的缓存项
    }

    /**
     * 删除所有缓存
     */
    public static void deleteCacheObject() {
        cache.invalidateAll();
    }

}
