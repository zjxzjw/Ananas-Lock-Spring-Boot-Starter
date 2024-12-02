package cn.zjx.ananas.annotation;

import java.lang.annotation.*;

/**
 * 锁注解
 *
 * @author 钟家兴
 * @date 2024/12/2 14:08
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnanasLock {

    /**
     * 锁的key
     */
    String[] keys();

    /**
     * 获取锁超时时间
     */
    long acquireTimeout() default 0L;

    /**
     * 错误提示
     */
    String errorMsg() default "";

    Class<?> exception();
}
