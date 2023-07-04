package com.luban.xbongbong.api.model.customform;

import com.luban.xbongbong.api.model.Response;
import lombok.Data;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomFormAlterResponse implements Response {
    private Integer code;
    private Long dataId;
    private String msg;
}
