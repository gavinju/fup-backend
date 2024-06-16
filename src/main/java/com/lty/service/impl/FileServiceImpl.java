package com.lty.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lty.common.DeleteRequest;
import com.lty.common.ErrorCode;
import com.lty.constant.FileProperties;
import com.lty.exception.BusinessException;
import com.lty.mapper.FileMapper;
import com.lty.model.dto.file.FileRequest;
import com.lty.model.entity.FileInfo;
import com.lty.model.entity.User;
import com.lty.service.FileService;
import com.lty.service.UserService;
import com.lty.utils.BaseUtil;
import com.lty.utils.ServletUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author lty
 */
@Slf4j
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FileInfo>
        implements FileService {

    @Resource
    private FileProperties fileProperties;

    @Resource
    private FileMapper fileMapper;

    @Resource
    private UserService userService;

    @Override
    public String upload(MultipartFile file) {
        //获取文件的名字,文件后缀
        String originName = file.getOriginalFilename();
        String suffix = originName.substring(originName.lastIndexOf("."));

        List<String> allSuffer = fileProperties.getSuffers();
        // 不允许上传的文件后缀
        if (!allSuffer.contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型不支持");
        }

        // 给上传的文件新建目录
        SimpleDateFormat sdf = new SimpleDateFormat(fileProperties.getPathFormat());
        String format = sdf.format(new Date());
        String realPath = fileProperties.getSavePath() + format;
        System.out.println("realPath:" + realPath);
        //若目录不存在则创建目录
        File folder = new File(realPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        //给上传文件取新的名字，避免重名
        String newName = IdUtil.simpleUUID() + suffix;
        try {
            //生成文件，folder为文件目录，newName为文件名
            file.transferTo(new File(folder, newName));
            HttpServletRequest request = ServletUtil.getRequest();
            //生成返回给前端的url
            String url = String.format("%s://%s:%d%s/file%s%s",
                    request.getScheme(),
                    request.getServerName(),
                    request.getServerPort(),
                    request.getContextPath(),
                    format,
                    newName);
            System.out.println("url:" + url);
            return url;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public FileInfo saveFile(MultipartFile file, FileRequest fileRequest) {
        String url = this.upload(file);
        // 设置新文件
        FileInfo newFileInfo = new FileInfo();
        newFileInfo.setUrl(url);
        newFileInfo.setSize(file.getSize());
        newFileInfo.setDescription(fileRequest.getDescription());
        newFileInfo.setType(fileRequest.getType());
        User currentUser = userService.getLoginUser();
        // 用户存在设置用户id
        if (currentUser != null) {
            newFileInfo.setUserId(currentUser.getId());
        }
        // 保存新文件
        this.save(newFileInfo);
        return newFileInfo;
    }

    @Override
    public boolean updateFile(FileRequest fileUpdateRequest) {
        String description = fileUpdateRequest.getDescription();
        String type = fileUpdateRequest.getType();

        if (StringUtils.isAllBlank(description, type)) {
            return true;
        }
        long id = fileUpdateRequest.getId();
        FileInfo oldFileInfo = this.getById(id);
        if (oldFileInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        FileInfo newFileInfo = new FileInfo();
        BeanUtils.copyProperties(oldFileInfo, newFileInfo);
        // 参数不为空才修改
        if (StringUtils.isNotBlank(description)) {
            newFileInfo.setDescription(description);
        }
        if (StringUtils.isNotBlank(type)) {
            newFileInfo.setType(type);
        }
        boolean b = this.updateById(newFileInfo);
        return b;
    }

    @Override
    public List<FileInfo> listFileInfo(FileRequest fileRequest) {
        QueryWrapper<FileInfo> qw = getQueryWrapper(fileRequest);
        List<FileInfo> fileInfoList = fileMapper.selectList(qw);
        return fileInfoList;
    }

    @Override
    public String getOriginPath(String fileUrl) {
        return fileProperties.getSavePath() + BaseUtil.getSaveUrl(fileUrl);
    }

    @Override
    public Boolean deleteFile(DeleteRequest deleteRequest) {
        long id = deleteRequest.getId();
        FileInfo oldFileInfo = this.getById(id);
        if (oldFileInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        String originPath = getOriginPath(oldFileInfo.getUrl());
        File oldFile = new File(originPath);
        if (oldFile.exists()) {
            log.info("deleteFile() called with parameters => [deleteRequest = {}]", originPath);
            oldFile.delete();
        }
        boolean b = this.removeById(id);
        return b;
    }

    @Override
    public QueryWrapper<FileInfo> getQueryWrapper(FileRequest fileRequest) {
        Long id = fileRequest.getId();
        String type = fileRequest.getType();
        String description = fileRequest.getDescription();
        QueryWrapper<FileInfo> qw = new QueryWrapper<>();
        qw.eq(id != null, "id", id);
        qw.like(StringUtils.isNotBlank(type), "type", type);
        qw.like(StringUtils.isNotBlank(description), "description", description);
        qw.orderByDesc("createTime");
        return qw;
    }
}
