package com.luban.xbongbong.api.model.customform;

import com.luban.xbongbong.api.model.Response;
import com.luban.xbongbong.api.model.common.list.XbbListModel;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2023/1/9
 */
@Data
public class XbbCustomFormListResponse implements Response {
    private List<XbbListModel> list;
    private Integer totalCount;
    private Integer totalPage;
}
