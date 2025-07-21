package com.sre.storage.bean;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author chen gang
 * @date 2025/4/2
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FileSaveResult extends FileResult{
    /**
     * 是否成功
     */
    private Boolean successFlag;
    /**
     * 失败信息
     */
    private String errorMsg;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件后缀名
     */
    private String fileExtensionName;


    public static FileSaveResult buildError(String errorMsg){
        FileSaveResult fileSaveResult = new FileSaveResult();
        fileSaveResult.setSuccessFlag(Boolean.FALSE);
        fileSaveResult.setErrorMsg(errorMsg);
        return fileSaveResult;
    }

    public static FileSaveResult buildSuccess(){
        FileSaveResult fileSaveResult = new FileSaveResult();
        fileSaveResult.setSuccessFlag(Boolean.TRUE);
        return fileSaveResult;
    }

}
