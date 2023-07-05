package com.luban.xbongbong.api.model.common.page;

import com.luban.xbongbong.api.model.Response;
import com.luban.xbongbong.api.model.common.list.XbbListItemModel;
import lombok.Data;

import java.util.List;

/**
 * @author hp
 */
@Data
public class XbbPageResponse implements Response {
    private List<XbbListItemModel> list;
    private Integer totalCount;
    private Integer totalPage;
}
