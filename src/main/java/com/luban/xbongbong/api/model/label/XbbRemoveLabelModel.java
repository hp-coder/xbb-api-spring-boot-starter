package com.luban.xbongbong.api.model.label;

import com.luban.xbongbong.api.model.BaseXbbModel;
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
public class XbbRemoveLabelModel extends BaseXbbModel {

    private List<Long> dataIdList;
    private List<Long> labelIds;
    private String attr;
    private Long formId;
}
