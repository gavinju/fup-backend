package com.lty.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 删除请求
 *
 * @author lty
 */
@Data
public class DeleteRequest implements Serializable {
    /**
     * id
     */
    private Long id;
}