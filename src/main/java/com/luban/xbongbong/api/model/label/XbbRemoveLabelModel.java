package com.luban.xbongbong.api.model.label;

import com.alibaba.fastjson.annotation.JSONField;
import com.luban.xbongbong.api.helper.enums.XbbFormBizType;
import com.luban.xbongbong.api.helper.serializer.XbbFormBizTypeSerializer;
import com.luban.xbongbong.api.model.XbbBaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author HP 2023/2/9
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class XbbRemoveLabelModel extends XbbBaseModel {

    private List<Long> dataIdList;
    private List<Long> labelIds;
    private String attr;
    private Long formId;
    @JSONField(serializeUsing = XbbFormBizTypeSerializer.class)
    private XbbFormBizType businessType;
}
