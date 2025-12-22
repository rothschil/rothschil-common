package io.github.rothschil.common.utils.cache;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * CTG-CACHE工具类
 */
@Slf4j
@Component
public class CacheUtils {

    /**
     * 最大失效时间 默认为 24小时
     */
    static int MAX_EXPIRED = 0X5265C00;


    /**
     * 默认15分钟，测试环境 5分钟
     */
    static int DEFALUT_EXPIRED = 0XDBBA0;

    /**
     * 默认5分钟
     */
    static int MAX_1ST = 0X493E0;


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
        set(k,v,DEFALUT_EXPIRED,enableCaffeine);
    }


    /**
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param k 键
     * @param v 值
     **/
    public static void set(String k, String v) {
        set(k,v,DEFALUT_EXPIRED,false);
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
            CaffeineCacheUtil.putIntoCache(k,v,MAX_1ST);
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
        if (expired < DEFALUT_EXPIRED) {
            expired = DEFALUT_EXPIRED;
        } else if (expired > MAX_EXPIRED) {
            expired = MAX_EXPIRED;
        }
        if (StringUtils.isBlank(v)) {
            log.warn("Key {} Value Is Null", k);
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
                log.info("[Hit Second Cache Class=Method] [Key]\n{} Cache Value ={}", k, val);
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

    /**
     * @param k 键
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     **/
    public static Object getObj(String k, Class<?> clazz) {
        try {
            String obj = get(k);
            log.debug("[Hit Second Cache Class=Method] [Key]\n{} Cache Value ={}", k, obj);
            if (ObjectUtil.isNotNull(obj)) {
                Object parse = JSON.parse(obj);
                return JSONObject.parseObject(parse.toString(), clazz);
            }
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            log.error("[缓存读取异常 根因异常类型] KEY={} {}", k, rootCause.getClass().getSimpleName());
            log.error("[缓存读取异常 根因异常消息] KEY={} {}", k, rootCause.getMessage());
        }
        return null;
    }


    public static void del(String k) {
        RedissonUtils.delete(k);
    }


    public long pttl(String k) {
        long var3 = 0L;
        try {
            var3 = RedissonUtils.getTimeToLive(k);
        } catch (Exception e) {
            Throwable rootCause = ExceptionUtils.getRootCause(e);
            log.error("[Cache ttl 根因异常类型] KEY {} {}", k, rootCause.getClass().getSimpleName());
            log.error("[Cache ttl 根因异常消息] KEY {} {}", k, rootCause.getMessage());
        }
        return var3;
    }



}
