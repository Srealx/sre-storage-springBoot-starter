package com.sre.storage.bean;

import lombok.Data;

import java.io.OutputStream;

/**
 *
 * @author chen gang
 * @date 2025/4/9
 */
@Data
public class OutputStreamResult {
    OutputStream fileOutputStream;
    /**
     * 是否成功
     */
    private Boolean successFlag;
    /**
     * 失败信息
     */
    private String errorMsg;

    public static OutputStreamResult buildError(String errorMsg){
        OutputStreamResult outputStreamResult = new OutputStreamResult();
        outputStreamResult.setSuccessFlag(Boolean.FALSE);
        outputStreamResult.setErrorMsg(errorMsg);
        return outputStreamResult;
    }

    public static OutputStreamResult buildSuccess(OutputStream fileOutputStream){
        OutputStreamResult outputStreamResult = new OutputStreamResult();
        outputStreamResult.setSuccessFlag(Boolean.TRUE);
        outputStreamResult.setFileOutputStream(fileOutputStream);
        return outputStreamResult;
    }
}
