package com.sre.storage.core;

import com.sre.storage.bean.FileResult;
import com.sre.storage.bean.FileSaveResult;
import com.sre.storage.bean.OutputStreamResult;
import com.sre.storage.service.IStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author chen gang
 * @date 2025/4/2
 */
@Slf4j
public class StorageServerProvider {

    private IStorageService storageService;

    protected StorageServerProvider() {

    }

    protected void setStorageService(IStorageService storageService){
        this.storageService = storageService;
        log.info("============   StorageServerProvider初始化成功   ============");
    }

    /**
     * 保存文件到主路径下
     * @param file 文件
     * @return {@link FileSaveResult}
     */
    public FileSaveResult uploadFile(File file){
        return storageService.uploadFile(file);
    }

    /**
     * 保存文件到主目录的子目录下
     * @param file 文件
     * @param subfolderName 子目录名
     * @return {@link FileSaveResult}
     */
    public  FileSaveResult uploadFile(File file, String subfolderName){
        return storageService.uploadFile(file,subfolderName);
    }

    /**
     * 保存MultipartFile
     * @param multipartFile {@link MultipartFile}
     * @return {@link FileSaveResult}
     */
    public  FileSaveResult uploadFile(MultipartFile multipartFile){
        return storageService.uploadFile(multipartFile);
    }

    /**
     * 保存MultipartFile到子目录下
     * @param multipartFile {@link MultipartFile}
     * @param subfolderName 子文件目录
     * @return {@link FileSaveResult}
     */
    public  FileSaveResult uploadFile(MultipartFile multipartFile,String subfolderName){
        return storageService.uploadFile(multipartFile,subfolderName);
    }

    /**
     * 上传文件到临时目录
     * @param file {@link File}
     * @return {@link FileSaveResult}
     */
    public  FileSaveResult uploadTempFile(File file){
        return storageService.uploadTempFile(file);
    }

    /**
     * 上传文件到临时目录
     * @param multipartFile {@link MultipartFile}
     * @return {@link FileSaveResult}
     */
    public  FileSaveResult uploadTempFile(MultipartFile multipartFile){
        return storageService.uploadTempFile(multipartFile);
    }

    /**
     * 获取文件信息
     * @param uuid 文件uuid
     * @return {@link FileSaveResult}
     */
    public  FileResult getFileResult(String uuid,String fileExtensionName){
        return storageService.getFileResult(uuid,fileExtensionName);
    }

    /**
     * 获取子文件夹中的文件信息
     * @param uuid 文件uuid
     * @param subfolderName 子文件夹名
     * @return {@link FileSaveResult}
     */
    public  FileResult getFileResult(String uuid,String subfolderName,String fileExtensionName){
        return storageService.getFileResult(uuid,subfolderName,fileExtensionName);
    }

    /**
     * 获取文件
     * @param uuid 文件uuid
     * @return {@link File}
     */
    public  File getFile(String uuid,String fileExtensionName){
        return storageService.getFile(uuid,fileExtensionName);
    }

    /**
     * 获取文件
     * @param uuid 文件uuid
     * @param subfolderName 子文件夹名
     * @return {@link File}
     */
    public  File getFile(String uuid,String subfolderName,String fileExtensionName){
        return storageService.getFile(uuid,subfolderName,fileExtensionName);
    }


    /**
     * 创建一个临时文件并提供outputstream, 该方法提供给需要自己来写文件的业务，如 excel导出
     * @param filename 文件名
     * @return {@link OutputStreamResult}
     */
    public OutputStreamResult createTempFileAndGetOutPutStream(String filename){
        return storageService.createTempFileAndGetOutPutStream(filename);
    }

    /**
     * 获取临时文件的存放路径
     * @return string
     */
    public String getTempFilePublicPath() {
        return storageService.getTempFilePublicPath();
    }

}
