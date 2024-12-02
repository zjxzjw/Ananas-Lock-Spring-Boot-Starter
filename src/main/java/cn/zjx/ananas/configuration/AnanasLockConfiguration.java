package cn.zjx.ananas.configuration;

import cn.zjx.ananas.annotation.EnableAnanasLock;
import cn.zjx.ananas.constant.StorageTypeEnum;
import cn.zjx.ananas.property.AnanasLockProperty;
import cn.zjx.ananas.utils.AnanasLockUtil;
import cn.zjx.ananas.utils.impl.AnanasLockUtilLocalImpl;
import cn.zjx.ananas.utils.impl.AnanasLockUtilRedisUtilImpl;
import io.micrometer.core.lang.Nullable;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 属性配置类
 *
 * @author 钟家兴
 * @date 2024/12/2 14:08
 */
@Configuration(value = "cn.zjx.ananas.AnanasLockConfiguration", proxyBeanMethods = false)
public class AnanasLockConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes enableAnanasLock;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableAnanasLock = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableAnanasLock.class.getName(), false)
        );
    }

    /**
     * 获取配置文件内配置信息
     */
    @Bean(name = "cn.zjx.ananas.AnanasLockProperty")
    @ConfigurationProperties(prefix = "ananas.lock")
    public AnanasLockProperty ananasLockProperty() {
        return new AnanasLockProperty();
    }

    /**
     * 根据配置获取对应锁工具类
     */
    @Bean(name = "cn.zjx.ananas.AnanasLockUtil")
    public AnanasLockUtil ananasLockUtil(AnanasLockProperty ananasLockProperty) {
        if (StorageTypeEnum.REDIS.equals(ananasLockProperty.getStorageType())) {
            return new AnanasLockUtilRedisUtilImpl();
        } else {
            return new AnanasLockUtilLocalImpl();
        }
    }
}
