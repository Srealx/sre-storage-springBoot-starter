package com.sre.storage.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author chengang
 * @Date 2025/7/21
 */
@Slf4j
public class FileUtil {
    public static boolean deleteFiles(File[] files){
        Boolean deleteFlag = Boolean.TRUE;
        for (File file : files) {
            if (file.isDirectory() && file.listFiles()!=null && file.listFiles().length>0){
                deleteFlag =  deleteFiles(file.listFiles());
            }
            if (Boolean.TRUE.equals(deleteFlag)){
                deleteFlag =  file.delete();
            }
        }
        return deleteFlag;
    }

    public static boolean  translateFile(File source, File dest){
        try (FileInputStream sourceFile = new FileInputStream(source);
             FileOutputStream destinationFile = new FileOutputStream(dest)){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = sourceFile.read(buffer)) > 0) {
                destinationFile.write(buffer, 0, length);
            }
        }catch (IOException e){
            log.error("文件拷贝异常, 源文件:{} , 目标文件:{} ", source.getPath(), dest.getPath(), e);
            return false;
        }
        return true;
    }
}
