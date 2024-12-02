package cn.zjx.ananas.property;

import cn.zjx.ananas.constant.StorageTypeEnum;
import lombok.Data;

/**
 * 锁配置属性
 *
 * @author 钟家兴
 * @date 2024/12/2 10:41
 */
@Data
public class AnanasLockProperty {

    /**
     * 存储类型
     */
    private StorageTypeEnum storageType = StorageTypeEnum.LOCAL;

    /**
     * 锁前缀
     */
    private String keyPrefix = "ananas:lock";
}
