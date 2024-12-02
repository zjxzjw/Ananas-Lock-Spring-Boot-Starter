package cn.zjx.ananas.configuration;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 优先级配置类
 *
 * @author 钟家兴
 * @date 2024/12/2 14:08
 */
public class AnanasLockConfigurationSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{
                AnanasLockConfiguration.class.getName()
        };
    }
}
