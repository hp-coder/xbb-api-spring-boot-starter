package com.luban.xbongbong.api.model;

import lombok.Data;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbResponse<T> {
    private Integer code;
    private String msg;
    private Boolean success;
    private T result;
}
