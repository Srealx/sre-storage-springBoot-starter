package com.sre.storage.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 本地存储配置bean
 * @author chen gang
 * @date 2025/4/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ToString
@ConfigurationProperties(prefix = "application.storage.local.config")
public class StorageLocalConfigBean extends StorageConfigBean{
    /**
     * 文件存储根路径
     */
    private String saveRootPath;
    /**
     * 临时文件存储路径
     */
    private String tempFileSavePath;
    /**
     * 对外公开路径
     */
    private String publicPath;
    /**
     * 临时文件公开路径
     */
    private String tempPublicPath;
}
