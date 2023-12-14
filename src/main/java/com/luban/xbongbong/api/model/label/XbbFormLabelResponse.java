package com.luban.xbongbong.api.model.label;

import com.alibaba.fastjson.annotation.JSONField;
import com.luban.xbongbong.api.helper.deserializer.XbbBizTypeDeserializer;
import com.luban.xbongbong.api.helper.enums.XbbBizType;
import com.luban.common.base.model.Response;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2023/2/2
 */
@Data
public class XbbFormLabelResponse implements Response {

    private List<LabelGroup> labelTree;

    @Data
    public static class LabelGroup {
        private Long id;
        private String labelGroupName;
        List<Label> labelEntities;
    }

    @Data
    public static class Label {
        private Long addTime;
        private Long appId;
        @JSONField(deserializeUsing = XbbBizTypeDeserializer.class)
        private XbbBizType businessType;
        private String color;
        private String corpid;
        private String creatorId;
        private Integer del;
        private Integer enable;
        private Long formId;
        private Long groupId;
        private Long id;
        private String name;
        private Integer sort;
        private Long updateTime;
    }
}
