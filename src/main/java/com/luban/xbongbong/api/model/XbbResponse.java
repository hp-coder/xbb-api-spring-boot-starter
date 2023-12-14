package com.luban.xbongbong.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.luban.common.base.model.Response;

/**
 * @author HP 2022/12/30
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XbbResponse<T extends Response> {
    private Integer code;
    private String msg;
    private Boolean success;
    private T result;
}
