package com.lty.service;

import com.lty.constant.FileProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.swing.*;

import static org.junit.Assert.*;

/**
 *
 * @author lty
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    @Resource
    private FileService fileService;

    @Resource
    private FileProperties fileProperties;

    @Test
    public void getOriginPath() {

        String url = "http://127.0.0.1:8088/api/file/2024/01/26/bd5934ccf5604ce1ac558669b0e48c13.png";
        // 正确路径：F:/project-space/file-cloud/file/2024/01/26/bd5934ccf5604ce1ac558669b0e48c13.png
        System.out.println(fileService.getOriginPath(url));
    }

    @Test
    public void getFileBlacklist() {
        System.out.println(fileProperties.getBlocked());
    }
}