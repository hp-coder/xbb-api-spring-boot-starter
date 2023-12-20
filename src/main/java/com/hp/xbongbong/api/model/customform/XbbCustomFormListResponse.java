package com.hp.xbongbong.api.model.customform;

import com.luban.common.base.model.Response;
import com.hp.xbongbong.api.model.common.list.XbbListItemModel;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2023/1/9
 */
@Data
public class XbbCustomFormListResponse implements Response {
    private List<XbbListItemModel> list;
    private Integer totalCount;
    private Integer totalPage;
}
