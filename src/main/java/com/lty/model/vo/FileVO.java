package com.lty.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class FileVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 文件地址
     */
    private String url;

    /**
     * 文件大小
     */
    private Long size;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 分类
     */
    private String type;

    /**
     * 描述
     */
    private String description;
}
