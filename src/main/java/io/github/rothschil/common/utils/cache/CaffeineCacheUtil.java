package io.github.rothschil.common.utils.cache;

import io.github.rothschil.common.config.cache.caffeine.CaffeineEntry;
import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;
import org.redisson.api.RBloomFilter;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Caffeine 一级缓存工具类
 * <p>
 * 采用懒加载获取 Spring Bean，避免静态初始化块在 Spring 上下文就绪前执行导致 NPE。
 *
 * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
 */
public class CaffeineCacheUtil {

    private static volatile Cache cache;

    private static volatile RBloomFilter BLOOM_FILTER;

    /**
     * 工具类禁止实例化
     */
    private CaffeineCacheUtil() {
    }

    /**
     * 懒加载 Caffeine Cache Bean，双重检查锁保证线程安全
     */
    private static Cache getCache() {
        if (cache == null) {
            synchronized (CaffeineCacheUtil.class) {
                if (cache == null) {
                    cache = SpringUtil.getBean(Cache.class);
                }
            }
        }
        return cache;
    }

    /**
     * 懒加载布隆过滤器 Bean，双重检查锁保证线程安全
     */
    private static RBloomFilter getBloomFilter() {
        if (BLOOM_FILTER == null) {
            synchronized (CaffeineCacheUtil.class) {
                if (BLOOM_FILTER == null) {
                    BLOOM_FILTER = SpringUtil.getBean(RBloomFilter.class);
                }
            }
        }
        return BLOOM_FILTER;
    }

    /**
     * 加入缓存，默认过期时间24小时且读后不刷新
     */
    public static void putIntoCache(String key, Object value) {
        putIntoCache(key, value, 24, TimeUnit.HOURS, false);
    }

    /**
     * 加入缓存，可指定过期时间，默认不刷新 默认是分钟
     */
    public static void putIntoCache(String key, Object value, long duration) {
        putIntoCache(key, value, duration, TimeUnit.MINUTES, false);
    }

    /**
     * 加入缓存，可指定过期时间、读后是否刷新
     */
    public static void putIntoCache(String key, Object value, long duration, TimeUnit timeUnit) {
        putIntoCache(key, value, duration, timeUnit, false);
    }

    /**
     * 加入缓存，可指定过期时间、读后是否刷新
     */
    public static void putIntoCache(String key, Object value, long duration, TimeUnit timeUnit, boolean accessFresh) {
        CaffeineEntry caffeineEntry = new CaffeineEntry();
        caffeineEntry.setKey(key);
        caffeineEntry.setValue(value);
        caffeineEntry.setExpireTime(timeUnit.toNanos(duration));
        caffeineEntry.setAccessFresh(accessFresh);
        getCache().put(key, caffeineEntry);
        // 同步写入布隆过滤器，否则读取时被前置拦截
        getBloomFilter().add(key);
    }

    /**
     * 获取缓存对象（带类型转换）
     */
    public static <T> T getCacheObject(String key, Class<T> clazz) {
        final Object result = getCacheObject(key);
        return Objects.nonNull(result) ? clazz.cast(result) : null;
    }

    /**
     * 获取缓存集合
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
     * <p>
     * 布隆过滤器前置拦截：contains 返回 false 表示一定不存在，可直接返回 null；
     * 返回 true 表示可能存在，继续查 Caffeine。
     */
    public static Object getCacheObject(String key) {
        // ========== 布隆过滤器前置拦截 ==========
        boolean mightContain = getBloomFilter().contains(key);
        if (!mightContain) {
            // 布隆判定一定不存在：直接返回 null，不查缓存
            return null;
        }

        final Object[] value = {null};
        CaffeineEntry entry = (CaffeineEntry) getCache().getIfPresent(key);
        Optional.ofNullable(entry)
                .ifPresent(item -> {
                    value[0] = item.getValue();
                });
        return value[0];
    }

    /**
     * 删除单个缓存
     */
    public static void deleteCacheObject(String key) {
        getCache().invalidate(key);
    }

    /**
     * 删除所有缓存
     */
    public static void deleteCacheObject() {
        getCache().invalidateAll();
    }

}
