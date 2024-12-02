package cn.zjx.ananas.annotation;

import cn.zjx.ananas.configuration.AnanasLockConfigurationSelector;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * 开启锁注解
 *
 * @author 钟家兴
 * @date 2024/12/2 14:08
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AnanasLockConfigurationSelector.class)
public @interface EnableAnanasLock {

    /**
     * 配置启动优先级
     */
    int order() default Ordered.LOWEST_PRECEDENCE;
}
