package io.github.rothschil.common.cache.redis.lock;

import io.github.rothschil.common.utils.RedisUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Redis分布式锁
 * 使用 SET resource-name anystring NX EX max-lock-time 实现
 * <p>
 * 该方案在 Redis 官方 SET 命令页有详细介绍。
 * http://doc.redisfans.com/string/set.html
 * <p>
 * 在介绍该分布式锁设计之前，我们先来看一下在从 Redis 2.6.12 开始 SET 提供的新特性，
 * 命令 SET key value [EX seconds] [PX milliseconds] [NX|XX]，其中：
 * <p>
 * EX seconds — 以秒为单位设置 key 的过期时间；
 * PX milliseconds — 以毫秒为单位设置 key 的过期时间；
 * NX — 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。
 * XX — 将key 的值设为value ，当且仅当key 存在，等效于 SETEX。
 * <p>
 * 命令 SET resource-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法。
 * <p>
 * 客户端执行以上的命令：
 * <p>
 * 如果服务器返回 OK ，那么这个客户端获得锁。
 * 如果服务器返回 NIL ，那么客户端获取锁失败，可以在稍后再重试。
 *
 * @author yuhao.wangwang
 * @version 1.0
 * @date 2017年11月3日 上午10:21:27
 */
public class RedisLock {

    private static Logger logger = LoggerFactory.getLogger(RedisLock.class);

    protected static RedissonClient REDISSON_CLIENT=RedisUtils.getClient();


    /**
     * 将key 的值设为value ，当且仅当key 不存在，等效于 SETNX。
     */
    public static final String NX = "NX";

    /**
     * seconds — 以秒为单位设置 key 的过期时间，等效于EXPIRE key seconds
     */
    public static final String EX = "EX";

    /**
     * 调用set后的返回值
     */
    public static final String OK = "OK";

    /**
     * 默认请求锁的超时时间(ms 毫秒)
     */
    private static final long TIME_OUT = 100;

    /**
     * 默认锁的有效时间(s)
     */
    public static final int EXPIRE = 60;




    /**
     * 锁标志对应的key
     */
    private String lockKey;



    /**
     * 锁的有效时间(s)
     */
    private int expireTime = EXPIRE;

    /**
     * 请求锁的超时时间(ms)
     */
    private long timeOut = TIME_OUT;

    /**
     * 锁标记
     */
//    private volatile boolean locked = false;

    final Random random = new Random();

    /**
     * 使用默认的锁过期时间和请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     */
    public RedisLock(String lockKey) {
        this.lockKey = lockKey + "_lock";
    }

    /**
     * 使用默认的请求锁的超时时间，指定锁的过期时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     */
    public RedisLock( String lockKey, int expireTime) {
        this(lockKey);
        this.expireTime = expireTime;
    }

    /**
     * 使用默认的锁的过期时间，指定请求锁的超时时间
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLock( String lockKey, long timeOut) {
        this(lockKey);
        this.timeOut = timeOut;
    }

    /**
     * 锁的过期时间和请求锁的超时时间都是用指定的值
     *
     * @param lockKey       锁的key（Redis的Key）
     * @param expireTime    锁的过期时间(单位：秒)
     * @param timeOut       请求锁的超时时间(单位：毫秒)
     */
    public RedisLock(String lockKey, int expireTime, long timeOut) {
        this(lockKey, expireTime);
        this.timeOut = timeOut;
    }


    /**
     * 尝试获取锁 立即返回
     *
     * @return 是否成功获得锁
     */
    public boolean lock(String lockKey) {
        RLock rLock = REDISSON_CLIENT.getLock(lockKey);
        rLock.lock();
        return rLock.isLocked();
    }


    /**
     * 解锁
     * <p>
     * 可以通过以下修改，让这个锁实现更健壮：
     * <p>
     * 不使用固定的字符串作为键的值，而是设置一个不可猜测（non-guessable）的长随机字符串，作为口令串（token）。
     * 不使用 DEL 命令来释放锁，而是发送一个 Lua 脚本，这个脚本只在客户端传入的值和键的口令串相匹配时，才对键进行删除。
     * 这两个改动可以防止持有过期锁的客户端误删现有锁的情况出现。
     */
    public Boolean unlock(String lockKey) {
        RLock rLock = REDISSON_CLIENT.getLock(lockKey);
        boolean flag = rLock.isLocked();
        if(!flag){
            rLock.unlock();
        }
        return true;
    }

    /**
     * 重写redisTemplate的set方法
     * <p>
     * 命令 SET resource-name anystring NX EX max-lock-time 是一种在 Redis 中实现锁的简单方法。
     * <p>
     * 客户端执行以上的命令：
     * <p>
     * 如果服务器返回 OK ，那么这个客户端获得锁。
     * 如果服务器返回 NIL ，那么客户端获取锁失败，可以在稍后再重试。
     *
     * @param key     锁的Key
     * @param value   锁里面的值
     * @param seconds 过去时间（秒）
     */
    private void set(final String key, final String value, final long seconds) {
        RedisUtils.setStr(key,value,seconds);
    }

    /**
     * @param millis 毫秒
     * @param nanos  纳秒
     * @Title: seleep
     * @Description: 线程等待时间
     * @author yuhao.wang
     */
    private void seleep(long millis, int nanos) {
        try {
            Thread.sleep(millis, random.nextInt(nanos));
        } catch (InterruptedException e) {
            logger.info("获取分布式锁休眠被中断：", e);
        }
    }

}