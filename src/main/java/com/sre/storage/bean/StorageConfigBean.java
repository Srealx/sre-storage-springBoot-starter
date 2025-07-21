package com.sre.storage.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author chen gang
 * @date 2025/4/2
 */
@Data
@ConfigurationProperties(prefix = "application.storage.config")
public class StorageConfigBean {
    /**
     * 关键核心参数, 区分每个存储服务实例的唯一id, 默认local
     */
    private String storageType = "local";

    /**
     * 是否开启自动配置
     */
    private Boolean enabled;

    /**
     * 是否开启分片上传服务
     */
    private Boolean enabledFragmentUpload;
}
