package cn.zjx.ananas.utils.impl;

import cn.zjx.ananas.utils.AnanasLockUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现
 *
 * @author 钟家兴
 * @date 2024/12/2 10:41
 */
public class AnanasLockUtilRedisUtilImpl implements AnanasLockUtil {

    /**
     * redis客户端
     */
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取锁
     */
    @Override
    public boolean tryLock(String key, long timeout, TimeUnit timeUnit) {
        // 获取锁对象
        RLock lock = redissonClient.getLock(key);

        // 锁结果
        boolean locked = false;

        try {
            // 尝试获取锁
            locked = lock.tryLock(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // 返回结果
        return locked;
    }

    /**
     * 释放锁
     */
    @Override
    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        lock.unlock();
    }
}
