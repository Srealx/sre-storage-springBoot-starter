package com.sre.storage.config;

import com.sre.storage.bean.StorageLocalConfigBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author chen gang
 * @date 2025/4/2
 */
@Configuration
@EnableConfigurationProperties(StorageLocalConfigBean.class)
public class StorageConfig {

}
