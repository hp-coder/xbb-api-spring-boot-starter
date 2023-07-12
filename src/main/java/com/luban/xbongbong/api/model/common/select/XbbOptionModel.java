package com.luban.xbongbong.api.model.common.select;

import lombok.Data;

/**
 * Example
 * <p>
 * "color":"#646566",
 * "checked":false,
 * "text":"2年送1年",
 * "value":"fe5dd8b7-5b7c-5b28-be6d-1c99a1dca879"
 *
 * @author hp
 */
@Data
public class XbbOptionModel {
    private String color;
    private boolean checked;
    private String text;
    private String value;
}
