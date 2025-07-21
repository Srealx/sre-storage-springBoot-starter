package com.sre.storage.core;

import com.sre.storage.bean.FileSaveResult;
import com.sre.storage.service.IFragmentUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 分片上传服务提供者
 * @Author chengang
 * @Date 2025/7/21
 */
@Slf4j
public class FragmentUploadServerProvider {
    IFragmentUpload iFragmentUpload;

    protected FragmentUploadServerProvider() {

    }

    protected void setStorageService(IFragmentUpload iFragmentUpload){
        this.iFragmentUpload = iFragmentUpload;
        log.info("============   FragmentUploadServerProvider 初始化成功   ============");
    }


    /**
     * 分片上传
     * @param file {@Link File}
     * @param uuid 分片上传的uuid, 用来识别分组
     * @param number 分片序号
     * @param mergeFlag 是否合并，上传结束的标记
     * @param fileExtensionName  文件名后缀, 用于合并分片
     * @return {@link FileSaveResult}
     */
    public FileSaveResult upload(File file, String uuid, int number , boolean mergeFlag, String fileExtensionName){
        return iFragmentUpload.upload(file,uuid,number,mergeFlag,fileExtensionName);
    }

    /**
     * 分片上传-指定子目录
     * @param file {@Link File}
     * @param uuid 分片上传的uuid, 用来识别分组
     * @param number 分片序号
     * @param mergeFlag 是否合并，上传结束的标记
     *                  * @param fileExtensionName  文件名后缀, 用于合并分片
     * @param subFolderName 指定上传的子文件夹
     * @return {@link FileSaveResult}
     */
    public FileSaveResult upload(File file, String uuid, int number , boolean mergeFlag, String fileExtensionName ,String  subFolderName){
        return iFragmentUpload.upload(file,uuid,number,mergeFlag,fileExtensionName,subFolderName);
    }


    /**
     * 分片上传-接收 MultipartFile
     * @param file {@Link File}
     * @param uuid 分片上传的uuid, 用来识别分组
     * @param number 分片序号
     * @param mergeFlag 是否合并，上传结束的标记
     * @param fileExtensionName  文件名后缀, 用于合并分片
     * @return {@link FileSaveResult}
     */
    public FileSaveResult upload(MultipartFile file, String uuid, int number , boolean mergeFlag, String fileExtensionName){
        return iFragmentUpload.upload(file,uuid,number,mergeFlag,fileExtensionName);
    }

    /**
     * 分片上传-接收 MultipartFile -指定子目录
     * @param file {@Link File}
     * @param uuid 分片上传的uuid, 用来识别分组
     * @param number 分片序号
     * @param mergeFlag 是否合并，上传结束的标记
     * @param fileExtensionName  文件名后缀, 用于合并分片
     * @param subFolderName 指定上传的子文件夹
     * @return {@link FileSaveResult}
     */
    public FileSaveResult upload(MultipartFile file, String uuid, int number , boolean mergeFlag, String fileExtensionName ,String  subFolderName){
        return iFragmentUpload.upload(file,uuid,number,mergeFlag,fileExtensionName,subFolderName);
    }


    /**
     * 完成文件校验
     * @param uuid 文件uuid
     * @param key 关键数据,如哈希值
     * @return
     */
    public boolean verifyFile(String uuid, String key){
        return iFragmentUpload.verifyFile(uuid,key);
    }


}
