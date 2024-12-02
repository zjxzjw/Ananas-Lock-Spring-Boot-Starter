package cn.zjx.ananas.utils;

import java.util.concurrent.TimeUnit;

/**
 * 锁工具类接口
 *
 * @author 钟家兴
 * @date 2024/12/2 10:41
 */
public interface AnanasLockUtil {

    /**
     * 获取锁
     */
    boolean tryLock(String key, long timeout, TimeUnit timeUnit);

    /**
     * 释放锁
     */
    void unlock(String key);
}
