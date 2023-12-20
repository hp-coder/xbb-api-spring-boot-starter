package com.hp.xbongbong.api.model.form;

import com.luban.common.base.model.Response;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author hp
 */
@Data
public class XbbFormFieldResponse implements Response {

    private List<XbbFormFieldModel> explainList;

    @Data
    public static class XbbFormFieldModel {
        private String attr;
        private String attrName;
        private Integer fieldType;
        private Integer noRepeat;
        private Integer required;
        private Integer showType;
        private Integer patternType;
        private String verifyRule;
        private Integer integerOnly;
        private Integer accuracy;
        private Integer numericalLimitsFlag;
        private Map<String, Object> numericalLimits;
        private String dateType;
        private List<XbbFormFieldItemModel> items;
        private Map<String, Object> serialNumber;
        private List<Map<String, Object>> subForm;
        private Integer multiple;
        private Integer amountFlag;
    }

    @Data
    public static class XbbFormFieldItemModel {
        private Boolean checked;
        private Integer isOther;
        private String text;
        private String value;
    }
}
