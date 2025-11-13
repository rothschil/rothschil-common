package io.github.rothschil.common.aspect;


import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import io.github.rothschil.common.annotation.Cacheable;
import io.github.rothschil.common.handler.AopSpelProcess;
import io.github.rothschil.common.utils.RedisUtils;
import io.github.rothschil.common.utils.ToolUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class CacheAspect {


    @Autowired
    private AopSpelProcess aopSpelProcess;

    @Autowired
    com.github.benmanes.caffeine.cache.Cache<String, Object> caffeineCache;

    @Around("@annotation(io.github.rothschil.common.annotation.Cacheable) && execution(* io.github.rothschil..*.*(..)))")
    public Object aroundCacheable(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String methodName = method.getName();
        String className = joinPoint.getTarget().getClass().getName();

        Cacheable cacheable = method.getAnnotation(Cacheable.class);
        boolean enableCaffeine = cacheable.enableCaffeine();
        // 从Caffeine缓存中获取数据
        String key = getKey(joinPoint);
        if(enableCaffeine){
            Object obj = caffeineCache.getIfPresent(key);
            log.info("[ Cache Class=Method] [Key] {}={}\n{} Cache Value ={}", className,methodName,key, obj);
            if (!ObjectUtil.isNull(obj)) {
                log.warn("[Hit First Cache Class=Method] [Key] {}={}\n{}={}", className,methodName,key, obj);
                return obj;
            }
        }
        String cacheResult = RedisUtils.getStr(key);
        if(StringUtils.isNotBlank(cacheResult)){
            assert caffeineCache != null;
            log.warn("[Hit Second Cache Class=Method] [Key] {}={}\n{}={}", className,methodName,key, cacheResult);
            Object obect = structure(methodSignature,cacheResult);
            if(enableCaffeine){
                caffeineCache.put(key, cacheResult);
            }
            return obect;
        }

        // 调用实际方法
        Object result = joinPoint.proceed();

        // 将结果放入Caffeine和Redis缓存
        if (caffeineCache != null) {
            if(enableCaffeine){
                caffeineCache.put(key, result);
            }
            String val = JSONObject.toJSONString(result);
            RedisUtils.setStr(key,val,20);
        }
        return result;
    }

    protected Object structure(MethodSignature methodSignature,String obj){
        Object parse = JSON.parse(obj);
        return JSONObject.parseObject(parse.toString(), methodSignature.getReturnType());
    }

    /** 获取缓存key
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param point
     * @return String
     **/
    public String getKey(ProceedingJoinPoint point) {
        MethodSignature methodSignature = (MethodSignature) point.getSignature();
        Method method = methodSignature.getMethod();
        boolean flag = false;
        //缓存key表达式、前缀获取
        String keyExpression = null;
        String pattern = "^#\\w*\\.\\w*";
        //获取Cacheable注解缓存key表达式、前缀
        if (method.isAnnotationPresent(Cacheable.class)) {
            Cacheable cacheable = method.getAnnotation(Cacheable.class);
            String keyOrigin = cacheable.key();
            if (ToolUtils.operation(keyOrigin, pattern)) {
                flag = true;
            }
             keyExpression = getAnnnotationValue(cacheable, point,pattern);
        }

        String key = null;
        //缓存key缺省为方法签名
        if (StrUtil.isEmpty(keyExpression) || point.getArgs().length == 0) {
            key = methodSignature.toString();
        } else {
            //解析缓存key表达式生成缓存key
            //方法参数对象构建
            if (!flag) {
                Map<String, Object> paramMap = new HashMap<>();
                for (int i = 0; i < point.getArgs().length; i++) {
                    String parameter = methodSignature.getParameterNames()[i];
                    Object arg = point.getArgs()[i];
                    paramMap.put(parameter, arg);
                }
                //根据方法参数对象解析缓存key表达式生成缓存key
                BeanPath beanPathResolver = new BeanPath(keyExpression);
                try {
                    key = beanPathResolver.get(paramMap).toString();
                } catch (NullPointerException e) {
                    log.error("[空指针异常 ] Params is {}", paramMap);
                }
            } else {
                key = keyExpression;
            }
        }
        //拼接前缀
        log.info("getKey key="+key);
        return key;
    }

    /** 获取存在Spel表达式的属性
     * @author <a href="mailto:WCNGS@QQ.COM">Sam</a>
     * @param annotation
     * @param joinPoint
     * @return String
     **/
    protected String getAnnnotationValue(Cacheable annotation, ProceedingJoinPoint joinPoint,String pattern) {
        String keyOirgin = ToolUtils.buildContent(annotation,pattern);
//        String content = annotation.key();
        List<String> templates = Lists.newArrayList(keyOirgin);
        templates = templates.stream().filter(StringUtils::isNotBlank).collect(Collectors.toList());
        HashMap<String, String> processMap = aopSpelProcess.processBeforeExec(templates, joinPoint);
        return processMap.get(keyOirgin);
    }

//    protected String getKey(String key){
//        if(!key.contains(SPLIT)){
//            return key;
//        }
//        ExpressionParser parser = new SpelExpressionParser();
////        IntfBo intfBo = new IntfBo("Nikola Tesla", "安徽合肥", "Serbian");
//
//        // 解析出一个表达式
//        Expression expression = parser.parseExpression(key);
//        // 开始准备表达式运行环境
//        EvaluationContext ctx = new StandardEvaluationContext();
//        ctx.setVariable("intfBo", intfBo);
//        String value = expression.getValue(ctx, String.class);
//    }
}
