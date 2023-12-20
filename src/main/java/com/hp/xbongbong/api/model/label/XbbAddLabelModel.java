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
 * @author HP 2023/2/2
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class XbbAddLabelModel extends AbstractXbbBaseModel {
    /**
     * 数据列表
     */
    private List<Long> dataIdList;
    /**
     * 标签id
     */
    private List<Long> labelIds;
    /**
     * 标签字段
     */
    private String attr;
    /**
     * 表单id
     */
    private Long formId;

    @JSONField(serializeUsing = XbbFormBizTypeSerializer.class)
    private XbbFormBizType businessType;
}
