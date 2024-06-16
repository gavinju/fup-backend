package com.lty.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lty.common.DeleteRequest;
import com.lty.model.dto.file.FileRequest;
import com.lty.model.entity.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author lty
 */
public interface FileService extends IService<FileInfo> {

    /**
     * 上传文件
     * @param file
     * @return url
     */
    String upload(MultipartFile file);

    /**
     * 上传文件并保存
     * @param file
     * @param fileRequest
     * @return
     */
    FileInfo saveFile(MultipartFile file, FileRequest fileRequest);

    /**
     * 修改文件
     * @param fileUpdateRequest
     * @return 布尔值(true 成功)
     */
    boolean updateFile(FileRequest fileUpdateRequest);

    /**
     * 获取查询后List
     * @param fileRequest
     * @return
     */
    List<FileInfo> listFileInfo(FileRequest fileRequest);

    /**
     * 获取查询条件
     * @param fileRequest
     * @return
     */
    Wrapper getQueryWrapper(FileRequest fileRequest);

    /**
     * 获取本地路径
     * @param fileUrl
     * @return
     */
    String getOriginPath(String fileUrl);

    Boolean deleteFile(DeleteRequest deleteRequest);
}
