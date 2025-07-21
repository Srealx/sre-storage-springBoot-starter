package com.sre.storage.bean;

import lombok.Data;

/**
 * @author chen gang
 * @date 2025/4/2
 */
@Data
public class FileResult {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 保存位置的子目录名
     */
    private String saveSubfolderName;
    /**
     * 对外公开访问地址
     */
    private String publicPath;
    /**
     * 保存的位置路径(如果文件保存在子目录下, 则包含子目录)
     */
    private String savePath;
}
