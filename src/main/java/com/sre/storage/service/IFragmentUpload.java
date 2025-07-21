package com.sre.storage.service;

import com.sre.storage.bean.FileSaveResult;
import com.sre.storage.bean.StorageConfigBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 接口-分片上传
 * @Author chengang
 * @Date 2025/7/21
 */
public interface IFragmentUpload<T extends StorageConfigBean> extends IStorageBase<T>{
    /**
     * 分片上传
     * @param file {@Link File}
     * @param uuid 分片上传的uuid, 用来识别分组
     * @param number 分片序号
     * @param mergeFlag 是否合并，上传结束的标记
     * @param fileExtensionName  文件名后缀, 用于合并分片
     * @return {@link FileSaveResult}
     */
    FileSaveResult upload(File file, String uuid, int number ,boolean mergeFlag, String fileExtensionName);

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
    FileSaveResult upload(File file, String uuid, int number , boolean mergeFlag, String fileExtensionName ,String  subFolderName);


    /**
     * 分片上传-接收 MultipartFile
     * @param file {@Link File}
     * @param uuid 分片上传的uuid, 用来识别分组
     * @param number 分片序号
     * @param mergeFlag 是否合并，上传结束的标记
     * @param fileExtensionName  文件名后缀, 用于合并分片
     * @return {@link FileSaveResult}
     */
    FileSaveResult upload(MultipartFile file, String uuid, int number , boolean mergeFlag, String fileExtensionName);

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
    FileSaveResult upload(MultipartFile file, String uuid, int number , boolean mergeFlag, String fileExtensionName ,String  subFolderName);


    /**
     * 完成文件校验
     * @param uuid 文件uuid
     * @param key 关键数据,如哈希值
     * @return
     */
    boolean verifyFile(String uuid, String key);
}
