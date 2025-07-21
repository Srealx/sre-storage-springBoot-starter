package com.sre.storage.config;

import com.sre.storage.bean.StorageConfigBean;
import com.sre.storage.bean.StorageLocalConfigBean;

/**
 * @author chen gang
 * @date 2025/4/2
 */
public enum StorageTypeEnum {
    /**
     *
     */
    LOCAL("local","本地", StorageLocalConfigBean.class),
    ;
    private final String type;
    private final String desc;

    private final Class<? extends StorageConfigBean> configBeanClass;

    StorageTypeEnum(String type, String desc,Class<? extends StorageConfigBean> configBeanClass) {
        this.type = type;
        this.desc = desc;
        this.configBeanClass = configBeanClass;
    }

    public String getType() {
        return type;
    }

    public Class<? extends StorageConfigBean> getConfigBeanClass(){
        return this.configBeanClass;
    }

    public static StorageTypeEnum findByType(String type){
        for (StorageTypeEnum value : StorageTypeEnum.values()) {
            if (value.type.equals(type)){
                return value;
            }
        }
        return null;
    }
}
