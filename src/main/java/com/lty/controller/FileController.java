package com.lty.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lty.annotation.AuthCheck;
import com.lty.common.*;
import com.lty.exception.BusinessException;
import com.lty.model.dto.file.FileRequest;
import com.lty.model.entity.FileInfo;
import com.lty.service.FileService;
import com.lty.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 文件上传
 * @author lty
 */
@Slf4j
@RequestMapping("/file")
@RestController
public class FileController {

    @Resource
    private FileService fileService;

    @Resource
    private UserService userService;

    @ApiOperation(value = "上传文件")
    @PostMapping("/upload")
    public BaseResponse<FileInfo> upload(@RequestPart(required = false) MultipartFile uploadFile, FileRequest fileRequest) {
        if (uploadFile == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "缺少文件参数");
        }
        FileInfo fileInfo = fileService.saveFile(uploadFile, fileRequest);
        return ResultUtils.success(fileInfo);
    }

    @ApiOperation(value = "分页获取")
    @GetMapping("/list/page")
    public BaseResponse<Page<FileInfo>> listFileByPage(FileRequest fileRequest, PageRequest pageRequest) {
        if (fileRequest == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR);
        }
        //分页基本字段
        long current = pageRequest.getCurrent();
        long size = pageRequest.getPageSize();
        // 反爬虫
        if (size > 100) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "查询数量过多");
        }
        Page<FileInfo> page = new Page<>(current, size);
        Wrapper queryWrapper = fileService.getQueryWrapper(fileRequest);
        Page<FileInfo> fileInfoPage = fileService.page(page, queryWrapper);
        return ResultUtils.success(fileInfoPage);
    }

    @ApiOperation(value = "列表获取(管理员可用)")
    @AuthCheck(mustRole = "admin")
    @GetMapping("/list")
    public BaseResponse<List<FileInfo>> listFile(FileRequest fileRequest) {
        List<FileInfo> fileInfoList = fileService.listFileInfo(fileRequest);
        return ResultUtils.success(fileInfoList);
    }

    @ApiOperation(value = "修改")
    @PostMapping("/update")
    public BaseResponse<Boolean> updateFile(@RequestBody FileRequest fileUpdateRequest) {
        boolean result = fileService.updateFile(fileUpdateRequest);
        return ResultUtils.success(result);
    }

    @ApiOperation(value = "删除")
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteFile(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean b = fileService.deleteFile(deleteRequest);
        return ResultUtils.success(b);
    }
}
