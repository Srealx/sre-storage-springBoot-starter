package com.sre.storage.service;

import com.sre.storage.bean.FileResult;
import com.sre.storage.bean.FileSaveResult;
import com.sre.storage.bean.OutputStreamResult;
import com.sre.storage.bean.StorageConfigBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author chen gang
 * @date 2025/4/2
 */
public interface IStorageService<T extends StorageConfigBean> extends IStorageBase<T>{

     /**
      * 保存文件到主路径下
      * @param file 文件
      * @return {@link FileSaveResult}
      */
     FileSaveResult uploadFile(File file);

     /**
      * 保存文件到主目录的子目录下
      * @param file 文件
      * @param subfolderName 子目录名
      * @return {@link FileSaveResult}
      */
     FileSaveResult uploadFile(File file, String subfolderName);

     /**
      * 保存MultipartFile
      * @param multipartFile {@link MultipartFile}
      * @return {@link FileSaveResult}
      */
     FileSaveResult uploadFile(MultipartFile multipartFile);

     /**
      * 保存MultipartFile到子目录下
      * @param multipartFile {@link MultipartFile}
      * @param subfolderName 子文件目录
      * @return {@link FileSaveResult}
      */
     FileSaveResult uploadFile(MultipartFile multipartFile,String subfolderName);

     /**
      * 上传文件到临时目录
      * @param file {@link File}
      * @return {@link FileSaveResult}
      */
     FileSaveResult uploadTempFile(File file);

     /**
      * 上传文件到临时目录
      * @param multipartFile {@link MultipartFile}
      * @return {@link FileSaveResult}
      */
     FileSaveResult uploadTempFile(MultipartFile multipartFile);

     /**
      * 获取文件信息
      * @param uuid 文件uuid
      * @return {@link FileSaveResult}
      */
     FileResult getFileResult(String uuid, String fileExtensionName);

     /**
      * 获取子文件夹中的文件信息
      * @param uuid 文件uuid
      * @param subfolderName 子文件夹名
      * @return {@link FileSaveResult}
      */
     FileResult getFileResult(String uuid,String subfolderName,String fileExtensionName);

     /**
      * 获取文件
      * @param uuid 文件uuid
      * @return {@link File}
      */
     File getFile(String uuid,String fileExtensionName);

     /**
      * 获取文件
      * @param uuid 文件uuid
      * @param subfolderName 子文件夹名
      * @return {@link File}
      */
     File getFile(String uuid,String subfolderName,String fileExtensionName);

     /**
      * 创建一个临时文件并提供outputstream, 该方法提供给需要自己来写文件的业务，如 excel导出
      * @param filename 文件名
      * @return {@link OutputStreamResult}
      */
     OutputStreamResult createTempFileAndGetOutPutStream(String filename);

     /**
      * 获取文件存储的公开路径
      * @return string
      */
     String getTempFilePublicPath();
}
