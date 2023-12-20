package com.hp.xbongbong.api.model.label;

import com.alibaba.fastjson.annotation.JSONField;
import com.hp.xbongbong.api.helper.enums.XbbFormBizType;
import com.hp.xbongbong.api.helper.serializer.XbbFormBizTypeSerializer;
import com.hp.xbongbong.api.model.AbstractXbbBaseModel;
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
public class XbbRemoveLabelModel extends AbstractXbbBaseModel {

    private List<Long> dataIdList;
    private List<Long> labelIds;
    private String attr;
    private Long formId;
    @JSONField(serializeUsing = XbbFormBizTypeSerializer.class)
    private XbbFormBizType businessType;
}
