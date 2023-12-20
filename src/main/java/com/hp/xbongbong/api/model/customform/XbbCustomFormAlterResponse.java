package com.hp.xbongbong.api.model.customform;

import com.luban.common.base.model.Response;
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
