package com.sre.storage.core;

import cn.hutool.core.collection.CollectionUtil;
import com.sre.storage.bean.StorageConfigBean;
import com.sre.storage.service.IFragmentUpload;
import com.sre.storage.service.IStorageBase;
import com.sre.storage.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@EnableConfigurationProperties(StorageConfigBean.class)
@ConditionalOnProperty(prefix = "application.storage.config", name = "enabled", havingValue = "true", matchIfMissing = true)
public class StorageAutoConfiguration implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(IStorageService.class)
    public StorageServerProvider storageServerProvider(
            StorageConfigBean properties,
            List<IStorageService> storageServices) {

        log.info(">>>>>>>>>>>>>> 文件存储服务自动配置：StorageServerProvider  <<<<<<<<<<<<<<");

        // 初始化所有存储服务
        Map<String, IStorageService> storageServiceMap = storageServices.stream()
                .peek(this::initializeStorageService)
                .filter(service -> service.getStorageConfig() != null)
                .collect(Collectors.toMap(
                        service -> service.getStorageConfig().getStorageType(),
                        Function.identity()
                ));

        if (CollectionUtil.isEmpty(storageServiceMap)) {
            throw new BeanCreationException("未找到可用的存储服务实现");
        }

        // 根据配置选择存储服务
        String storageType = properties.getStorageType();
        IStorageService selectedService = storageServiceMap.get(storageType);

        if (selectedService == null) {
            throw new BeanCreationException("找不到类型为 " + storageType + " 的存储服务");
        }

        // 创建并初始化提供者
        StorageServerProvider provider = new StorageServerProvider();
        provider.setStorageService(selectedService);

        log.info(">>>>>>>>>>>>>> 文件存储服务初始化成功 [类型: {}] <<<<<<<<<<<<<<", storageType);
        return provider;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnClass(IFragmentUpload.class)
    @ConditionalOnProperty(prefix = "application.storage.config", name = "enabledFragmentUpload", havingValue = "true", matchIfMissing = true)
    public FragmentUploadServerProvider fragmentUploadProvider(
            StorageConfigBean properties,
            List<IFragmentUpload> fragmentUploads) {
        log.info(">>>>>>>>>>>>>> 文件存储服务自动配置：FragmentUploadServerProvider  <<<<<<<<<<<<<<");

        Map<String, IFragmentUpload> fragmentUploadMap = fragmentUploads.stream()
                .peek(this::initializeStorageService)
                .filter(service -> service.getStorageConfig() != null)
                .collect(Collectors.toMap(
                        service -> service.getStorageConfig().getStorageType(),
                        Function.identity()
                ));
        if (CollectionUtil.isEmpty(fragmentUploadMap)) {
            throw new BeanCreationException("未找到可用的存储服务实现");
        }

        // 根据配置选择存储服务
        String storageType = properties.getStorageType();
        IFragmentUpload selectedService = fragmentUploadMap.get(storageType);

        if (selectedService == null) {
            throw new BeanCreationException("找不到类型为 " + storageType + " 的分片上传服务");
        }

        // 创建并初始化提供者
        FragmentUploadServerProvider provider = new FragmentUploadServerProvider();
        provider.setStorageService(selectedService);

        log.info(">>>>>>>>>>>>>> 分片上传服务初始化成功 [类型: {}] <<<<<<<<<<<<<<", storageType);
        return provider;
    }

    /**
     * 初始化存储服务（注入配置）
     */
    private void initializeStorageService(IStorageBase service) {
        try {
            for (Type type : service.getClass().getGenericInterfaces()) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType pType = (ParameterizedType) type;
                    if (pType.getRawType().equals(IStorageBase.class)) {
                        Type[] args = pType.getActualTypeArguments();
                        if (args.length > 0 && args[0] instanceof Class) {
                            Class<?> configClass = (Class<?>) args[0];
                            if (StorageConfigBean.class.isAssignableFrom(configClass)) {
                                StorageConfigBean storageConfigBean = (StorageConfigBean)applicationContext.getBean(configClass);
                                service.setStorageConfig(storageConfigBean);
                            }
                        }
                    }
                }
            }
        } catch (BeansException e) {
            log.error("存储服务初始化失败: {}", service.getClass().getSimpleName(), e);
        }
    }
}