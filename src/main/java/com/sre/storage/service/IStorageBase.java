package com.sre.storage.service;

import com.sre.storage.bean.StorageConfigBean;

/**
 * 存储服务基础接口
 * @Author chengang
 * @Date 2025/5/26
 */
public interface IStorageBase<T extends StorageConfigBean>{
    /**
     * 设置存储的配置信息
     * @param t
     */
    void setStorageConfig(T t);

    /**
     * 获取存储配置
     * @return t
     */
    T getStorageConfig();
}
