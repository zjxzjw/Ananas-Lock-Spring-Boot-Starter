package cn.zjx.ananas.aspect.context;

import lombok.Data;

/**
 * 分布式锁上下文
 *
 * @author 钟家兴
 * @date 2024/12/2 14:08
 */
@Data
public class AnanasLockContext {

    /**
     * key
     */
    private String key;

}
