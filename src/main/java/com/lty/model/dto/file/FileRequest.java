package com.lty.model.dto.file;

import lombok.Data;

import java.io.Serializable;

/**
 * @author lty
 */
@Data
public class FileRequest implements Serializable {

    private Long id;

    private String type;

    private String description;
}
