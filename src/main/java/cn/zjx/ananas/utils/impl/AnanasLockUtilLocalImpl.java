package cn.zjx.ananas.utils.impl;

import cn.zjx.ananas.utils.AnanasLockUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 本地锁实现
 *
 * @author 钟家兴
 * @date 2024/12/2 10:41
 */
public class AnanasLockUtilLocalImpl implements AnanasLockUtil {

    /**
     * 本地锁池
     */
    private final Map<String, Lock> lockMap = new HashMap<>();

    /**
     * 获取锁
     */
    @Override
    public boolean tryLock(String key, long timeout, TimeUnit timeUnit) {
        // 获取锁对象
        Lock lock = getLock(key);

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
        // 获取锁对象
        Lock lock = getLock(key);

        // 锁池中移除锁
        lockMap.remove(key);

        // 释放锁
        lock.unlock();
    }

    /**
     * 获取锁对象
     */
    private Lock getLock(String key) {
        // 获取锁对象
        return lockMap.computeIfAbsent(key, k -> new ReentrantLock());
    }
}
