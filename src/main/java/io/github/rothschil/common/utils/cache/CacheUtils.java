package io.github.rothschil.common.utils.cache;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * CTG-CACHE工具类
 */
@Slf4j
@Component
public class CacheUtils {


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     * @param v 值
     * @param enableCaffeine    是否启用二级Caffeine，true 启用
     **/
    public static void set(String k, String v,boolean enableCaffeine) {
        if(enableCaffeine){
            CaffeineCacheUtil.putIntoCache(k,v,60);
        }
        set(k,v,50000,enableCaffeine);
    }


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     * @param v 值
     **/
    public static void set(String k, String v) {
        set(k,v,50000,false);
    }


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     * @param v 值
     * @param expired   失效时间，单位毫秒
     * @param enableCaffeine    是否启用二级Caffeine，true 启用
     **/
    public static void set(String k, String v, Integer expired, boolean enableCaffeine) {
        if(enableCaffeine){
            CaffeineCacheUtil.putIntoCache(k,v,60,TimeUnit.MINUTES);
        }
        set(k,v,expired);
    }


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     * @param v 值
     * @param expired   失效时间，单位毫秒
     **/
    public static void set(String k, String v, Integer expired) {
        int max = 50000;
        if(expired<max){
            expired = max;
        }
        if (StringUtils.isBlank(v)) {
            log.warn("key:{},value为null", k);
            return ;
        }
        RedissonUtils.setIfAbsent(k,v,expired);
    }

    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     * @param enableCaffeine    是否启用二级Caffeine，true 启用
     **/
    public static String get(String k,boolean enableCaffeine) {

        Object obj=null;
        if(enableCaffeine){
            obj = CaffeineCacheUtil.getCacheObject(k);
            if(ObjectUtil.isNotNull(obj)){
                log.warn("[Hit First Cache Class=Method] [Key] \n{}={}", k, obj);
            } else{
                log.debug("[Missed the level 1st cache] [Key]={}", k);
            }
        }
        if(ObjectUtil.isNull(obj)){
            String val = get(k);
            if(!StringUtils.isBlank(val)){
                log.info("[Hit Second Cache Class=Method] [Key]\n{} Cache Value ={}", k, obj);
                CaffeineCacheUtil.putIntoCache(k,val,60,TimeUnit.MINUTES);
            } else{
                log.debug("[Missed the level 2nd cache] [Key]={}", k);
            }
            return val;
        } else{
            return obj.toString();
        }
    }

    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     **/
    public static String get(String k) {
        return RedissonUtils.getCacheObject(k);
    }


    public static void del(String k) {
        RedissonUtils.delete(k);
    }




}
