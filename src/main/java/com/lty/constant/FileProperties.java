package com.lty.constant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lty
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileProperties {

    /**
     * 保存文件路径，默认为项目文件夹下file目录下(注意带/)
     */
    private String savePath = BaseConstant.PROJECT_ROOT_DIRECTORY + "/file/";

    /**
     * 允许文件后缀
     */
    private List<String> suffers = new ArrayList<>();

    /**
     * 黑名单
     */
    private List<String> blocked = new ArrayList<>();

    /**
     * 文件上传路径格式，默认为/yyyy/MM/dd/
     */
    private String pathFormat = "/yyyy/MM/dd/";
}
