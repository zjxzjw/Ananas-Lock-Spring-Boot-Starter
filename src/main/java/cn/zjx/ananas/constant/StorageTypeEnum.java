package cn.zjx.ananas.constant;

import lombok.Getter;

/**
 * 存储类型枚举
 *
 * @author 钟家兴
 * @date 2024/12/2 10:41
 */
@Getter
public enum StorageTypeEnum {

    LOCAL("local"),
    REDIS("redis");

    private final String type;

    StorageTypeEnum(String type) {
        this.type = type;
    }
}
