package com.sre.storage.service;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.text.StrPool;
import com.binninfo.commonBase.utils.UuidUtils;
import com.sre.storage.bean.FileResult;
import com.sre.storage.bean.FileSaveResult;
import com.sre.storage.bean.OutputStreamResult;
import com.sre.storage.bean.StorageLocalConfigBean;
import com.sre.storage.utils.FileUtil;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author chen gang
 * @date 2025/4/2
 */
@Service
@Slf4j
public class LocalStorageServiceImpl implements IStorageService<StorageLocalConfigBean>,IFragmentUpload<StorageLocalConfigBean>{
    /**
     * 存储配置
     */
    private static final String STORAGE_TYPE = "local";
    private StorageLocalConfigBean storageLocalConfigBean;

    @Override
    public void setStorageConfig(StorageLocalConfigBean storageLocalConfigBean) {
        if (storageLocalConfigBean == null ||
                StringUtil.isEmpty(storageLocalConfigBean.getPublicPath()) ||
                StringUtil.isEmpty(storageLocalConfigBean.getSaveRootPath())){
            log.error("LocalStorageServiceImpl 初始化失败,  配置参数不完整, 请检查配置");
            log.info("storageLocalConfigBean: "+this.storageLocalConfigBean.toString());
            throw new BeanCreationException("LocalStorageServiceImpl初始化异常");
        }
        storageLocalConfigBean.setStorageType(STORAGE_TYPE);
        this.storageLocalConfigBean = storageLocalConfigBean;
        log.info("------- >>>> LocalStorageServiceImpl 初始化成功  <<<< -------");
    }

    @Override
    public StorageLocalConfigBean getStorageConfig(){
        return this.storageLocalConfigBean;
    }

    @Override
    public FileSaveResult uploadFile(File file) {
        if (file == null){
            return FileSaveResult.buildError("文件为空, 无法上传");
        }
        String filename = file.getName();
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        if (CharSequenceUtil.isEmpty(extName)) {
            return FileSaveResult.buildError("上传的文件格式有误, 未读取到正确的文件格式后缀");
        }
        String uuid = UuidUtils.genFileUUid();
        return uploadFile(file,filename,extName,uuid,storageLocalConfigBean.getSaveRootPath(),Boolean.FALSE);
    }

    @Override
    public FileSaveResult uploadFile(File file, String subfolderName) {
        if (file == null){
            return FileSaveResult.buildError("文件为空, 无法上传");
        }
        if (Boolean.FALSE.equals(subfolderName.startsWith(StrPool.SLASH))
        || Boolean.TRUE.equals(subfolderName.endsWith(StrPool.SLASH))){
            return FileSaveResult.buildError("子目录必须以 / 开头且不能以 / 结尾");
        }
        File folder = new File(storageLocalConfigBean.getSaveRootPath() + subfolderName);
        if (Boolean.FALSE.equals(folder.exists())){
            boolean mkdir = folder.mkdir();
            if (Boolean.FALSE.equals(mkdir)){
                return FileSaveResult.buildError("创建子目录失败");
            }
        }
        String filename = file.getName();
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        if (CharSequenceUtil.isEmpty(extName)) {
            return FileSaveResult.buildError("上传的文件格式有误, 未读取到正确的文件格式后缀");
        }
        String uuid = UuidUtils.genFileUUid();
        return uploadFile(file,filename,extName,uuid,storageLocalConfigBean.getSaveRootPath()+subfolderName,Boolean.FALSE);
    }

    @Override
    public FileSaveResult uploadFile(MultipartFile multipartFile) {
        if (multipartFile == null){
            return FileSaveResult.buildError("文件为空, 无法上传");
        }
        String filename = multipartFile.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)){
            return FileSaveResult.buildError("文件名为空,无法上传");
        }
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        if (CharSequenceUtil.isEmpty(extName)) {
            return FileSaveResult.buildError("上传的文件格式有误, 未读取到正确的文件格式后缀");
        }
        String uuid = UuidUtils.genFileUUid();
        return uploadFile(multipartFile,filename,extName,uuid,storageLocalConfigBean.getSaveRootPath(),Boolean.FALSE);
    }

    @Override
    public FileSaveResult uploadFile(MultipartFile multipartFile, String subfolderName) {
        if (multipartFile == null){
            return FileSaveResult.buildError("文件为空, 无法上传");
        }
        if (Boolean.FALSE.equals(subfolderName.startsWith(StrPool.SLASH))
                || Boolean.TRUE.equals(subfolderName.endsWith(StrPool.SLASH))){
            return FileSaveResult.buildError("子目录必须以 / 开头且不能以 / 结尾");
        }
        File folder = new File(storageLocalConfigBean.getSaveRootPath() + subfolderName);
        if (Boolean.FALSE.equals(folder.exists())){
            boolean mkdir = folder.mkdir();
            if (Boolean.FALSE.equals(mkdir)){
                return FileSaveResult.buildError("创建子目录失败");
            }
        }
        String filename = multipartFile.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)){
            return FileSaveResult.buildError("文件名为空,无法上传");
        }
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        if (CharSequenceUtil.isEmpty(extName)) {
            return FileSaveResult.buildError("上传的文件格式有误, 未读取到正确的文件格式后缀");
        }
        String uuid = UuidUtils.genFileUUid();
        return uploadFile(multipartFile,filename,extName,uuid,storageLocalConfigBean.getSaveRootPath()+subfolderName,Boolean.FALSE);
    }

    @Override
    public FileSaveResult uploadTempFile(File file) {
        if (StringUtil.isEmpty(storageLocalConfigBean.getTempFileSavePath())
        || StringUtil.isEmpty(storageLocalConfigBean.getTempPublicPath())){
            return FileSaveResult.buildError("临时目录配置不完整，无法上传");
        }
        if (file == null){
            return FileSaveResult.buildError("文件为空, 无法上传");
        }
        String filename = file.getName();
        if (CharSequenceUtil.isBlank(filename)){
            return FileSaveResult.buildError("文件名为空,无法上传");
        }
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        if (CharSequenceUtil.isEmpty(extName)) {
            return FileSaveResult.buildError("上传的文件格式有误, 未读取到正确的文件格式后缀");
        }
        String uuid = UuidUtils.genFileUUid();
        return uploadFile(file,filename,extName,uuid,storageLocalConfigBean.getTempFileSavePath(),Boolean.TRUE);
    }

    @Override
    public FileSaveResult uploadTempFile(MultipartFile multipartFile) {
        if (StringUtil.isEmpty(storageLocalConfigBean.getTempFileSavePath())
                || StringUtil.isEmpty(storageLocalConfigBean.getTempPublicPath())){
            return FileSaveResult.buildError("临时目录配置不完整，无法上传");
        }
        if (multipartFile == null){
            return FileSaveResult.buildError("文件为空, 无法上传");
        }
        String filename = multipartFile.getOriginalFilename();
        if (CharSequenceUtil.isBlank(filename)){
            return FileSaveResult.buildError("文件名为空,无法上传");
        }
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        if (CharSequenceUtil.isEmpty(extName)) {
            return FileSaveResult.buildError("上传的文件格式有误, 未读取到正确的文件格式后缀");
        }
        String uuid = UuidUtils.genFileUUid();
        return uploadFile(multipartFile,filename,extName,uuid,storageLocalConfigBean.getTempFileSavePath(),Boolean.TRUE);
    }

    @Override
    public FileResult getFileResult(String uuid, String fileExtensionName) {
        FileResult fileResult = new FileResult();
        fileResult.setUuid(uuid);
        fileResult.setSavePath(storageLocalConfigBean.getSaveRootPath() + StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
        fileResult.setPublicPath(storageLocalConfigBean.getPublicPath() + StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
        return fileResult;
    }

    @Override
    public FileResult getFileResult(String uuid, String subfolderName,String fileExtensionName) {
        FileResult fileResult = new FileResult();
        fileResult.setUuid(uuid);
        fileResult.setSavePath(storageLocalConfigBean.getSaveRootPath() + subfolderName + StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
        fileResult.setPublicPath(storageLocalConfigBean.getPublicPath() + subfolderName +StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
        return fileResult;
    }

    @Override
    public File getFile(String uuid,String fileExtensionName) {
        return new File(storageLocalConfigBean.getSaveRootPath() + StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
    }

    @Override
    public File getFile(String uuid, String subfolderName,String fileExtensionName) {
        return new File(storageLocalConfigBean.getSaveRootPath() + subfolderName+ StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
    }

    @Override
    public OutputStreamResult createTempFileAndGetOutPutStream(String filename) {
        if (StringUtil.isEmpty(storageLocalConfigBean.getTempFileSavePath())
                || StringUtil.isEmpty(storageLocalConfigBean.getTempPublicPath())){
            return OutputStreamResult.buildError("临时目录配置不完整，无法创建文件输出流");
        }
        if (StringUtil.isEmpty(filename)){
            return OutputStreamResult.buildError("文件名为空,无法创建文件输出流");
        }
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(storageLocalConfigBean.getTempFileSavePath() +  StrPool.SLASH + filename));
            return OutputStreamResult.buildSuccess(bufferedOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return OutputStreamResult.buildError("创建输出流失败");
        }
    }

    @Override
    public String getTempFilePublicPath() {
        return storageLocalConfigBean.getTempPublicPath();
    }


    /**
     * 将文件写到本地
     * @param file 文件
     * @param filename 文件名
     * @param extName 文件拓展名
     * @param uuid 文件uuid
     * @param savePath 保存路径
     * @return {@link FileSaveResult}
     */
    private FileSaveResult uploadFile(File file,String filename,String extName,String uuid,String savePath, Boolean tempFlag){
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(savePath +  StrPool.SLASH + uuid + StrPool.DOT + extName))){
            byte[] bb = new byte[1024];
            int n;// 每次读取到的字节数组的长度
            while ((n = bufferedInputStream.read(bb)) != -1) {
                bufferedOutputStream.write(bb, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件保存失败:  ",e);
            return FileSaveResult.buildError("文件上传失败");
        }
        FileSaveResult fileSaveResult = FileSaveResult.buildSuccess();
        fileSaveResult.setFileName(filename);
        fileSaveResult.setFileExtensionName(extName);
        fileSaveResult.setUuid(uuid);
        if (Boolean.TRUE.equals(tempFlag)){
            fileSaveResult.setPublicPath(storageLocalConfigBean.getTempPublicPath() + StrPool.SLASH + uuid + StrPool.DOT + extName);
        }else {
            fileSaveResult.setPublicPath(storageLocalConfigBean.getPublicPath() + StrPool.SLASH + uuid + StrPool.DOT + extName);
        }

        return fileSaveResult;
    }


    /**
     * 将文件写到本地
     * @param file 文件
     * @param filename 文件名
     * @param extName 文件拓展名
     * @param uuid 文件uuid
     * @param savePath 保存路径
     * @return {@link FileSaveResult}
     */
    private FileSaveResult uploadFile(MultipartFile file,String filename,String extName,String uuid,String savePath, Boolean tempFlag){
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(savePath +  StrPool.SLASH + uuid + StrPool.DOT + extName))){
            byte[] bb = new byte[1024];
            int n;// 每次读取到的字节数组的长度
            while ((n = bufferedInputStream.read(bb)) != -1) {
                bufferedOutputStream.write(bb, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("文件保存失败:  ",e);
            return FileSaveResult.buildError("文件上传失败");
        }
        FileSaveResult fileSaveResult = FileSaveResult.buildSuccess();
        fileSaveResult.setFileName(filename);
        fileSaveResult.setFileExtensionName(extName);
        fileSaveResult.setUuid(uuid);
        if (Boolean.TRUE.equals(tempFlag)){
            fileSaveResult.setPublicPath(storageLocalConfigBean.getTempPublicPath() + StrPool.SLASH + uuid + StrPool.DOT + extName);
        }else {
            fileSaveResult.setPublicPath(storageLocalConfigBean.getPublicPath() + StrPool.SLASH + uuid + StrPool.DOT + extName);
        }
        return fileSaveResult;
    }


    @Override
    public FileSaveResult upload(File file, String uuid,  int number ,boolean mergeFlag, String fileExtensionName) {
        return upload(file,uuid,number,mergeFlag,fileExtensionName,"");
    }

    @Override
    public FileSaveResult upload(File file, String uuid, int number , boolean mergeFlag, String fileExtensionName, String subFolderName) {
        String rootPath = storageLocalConfigBean.getSaveRootPath()+(StringUtil.isNotEmpty(subFolderName)? StrPool.SLASH + subFolderName:"") + uuid;
        boolean mkdirFlag = uploadMkdir(rootPath);
        if (Boolean.FALSE.equals(mkdirFlag)){
            return FileSaveResult.buildError("文件夹创建失败");
        }
        File uuidFolder = new File(rootPath);
        boolean translateFlag = FileUtil.translateFile(file, new File(rootPath + StrPool.SLASH + file.getName() + number + ".part"));
        if (Boolean.FALSE.equals(translateFlag)){
            return FileSaveResult.buildError("文件上传失败");
        }
        if (Boolean.TRUE.equals(mergeFlag)){
            return merge(uuid,uuidFolder,rootPath,fileExtensionName);
        }
        return FileSaveResult.buildSuccess();
    }

    @Override
    public FileSaveResult upload(MultipartFile file, String uuid,  int number ,boolean mergeFlag, String fileExtensionName) {
        return upload(file,uuid,number,mergeFlag,fileExtensionName,"");
    }

    @Override
    public FileSaveResult upload(MultipartFile file, String uuid,  int number ,boolean mergeFlag, String fileExtensionName, String subFolderName) {
        String rootPath = storageLocalConfigBean.getSaveRootPath()+(StringUtil.isNotEmpty(subFolderName)? StrPool.SLASH+subFolderName:"") + uuid;
        boolean mkdirFlag = uploadMkdir(rootPath);
        if (Boolean.FALSE.equals(mkdirFlag)){
            return FileSaveResult.buildError("文件夹创建失败");
        }
        File uuidFolder = new File(rootPath);
        try {
            file.transferTo(new File(rootPath + StrPool.SLASH + file.getOriginalFilename() + number + ".part"));
        } catch (IOException e) {
            return FileSaveResult.buildError("文件传输异常: "  + e.getMessage());
        }
        if (Boolean.TRUE.equals(mergeFlag)){
            return merge(uuid,uuidFolder,rootPath,fileExtensionName);
        }
        return FileSaveResult.buildSuccess();
    }

    private boolean uploadMkdir(String path){
        File uuidFolder = new File(path);
        if (Boolean.FALSE.equals(uuidFolder.exists())){
            return uuidFolder.mkdirs();
        }
        return Boolean.TRUE;
    }

    private FileSaveResult merge(String uuid, File uuidFolder, String rootPath, String fileExtensionName){
        try {
            List<File> chunks = Arrays.stream(Objects.requireNonNull(uuidFolder.listFiles()))
                    // 排序文件片, 按顺序合并
                    .sorted(Comparator.comparingInt(f -> Integer.parseInt(com.sre.storage.utils.StringUtil.getTrailingNumbers(f.getName().split("\\.")[0]))))
                    .collect(Collectors.toList());
            // 合并文件
            File outputFile = new File(rootPath + StrPool.SLASH + uuid + StrPool.DOT + fileExtensionName);
            try (FileChannel outChannel = new FileOutputStream(outputFile).getChannel()){
                for (File chunk : chunks) {
                    try (FileChannel inChannel = new FileInputStream(chunk).getChannel();){
                        inChannel.transferTo(0, inChannel.size(), outChannel);
                    }
                }
            }
        } catch (Exception e){
            log.error("合并分片文件失败 :  ",e);
            return FileSaveResult.buildError("分片合并失败: "+e.getMessage());
        }finally {
            // 删除整个分片文件夹
            boolean tempFolderDelete = FileUtil.deleteFiles(new File[]{uuidFolder});
            if (Boolean.FALSE.equals(tempFolderDelete)){
                log.error("删除分片临时文件夹失败, path: {}, name:{}",uuidFolder.getPath(),uuidFolder.getName());
            }
        }
        return FileSaveResult.buildSuccess();
    }

    @Override
    public boolean verifyFile(String uuid, String key) {
        return false;
    }
}
