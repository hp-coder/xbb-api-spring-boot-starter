package com.luban.xbongbong.api.model.common.owner;

import lombok.Data;

/**
 * Example
 * <p>
 * "name":"胡鹏",
 * "avatar":"https://static-legacy.dingtalk.com/media/lADPD4Bh13s0Fo3NBJLNBJI_1170_1170.jpg",
 * "id":"xxxxx"
 *
 * @author hp
 */
@Data
public class XbbOwnerModel {
    private String name;
    private String avatar;
    private String id;
}
