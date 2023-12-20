package com.hp.xbongbong.api.model.common.address;

import lombok.Data;

/**
 * Example
 *
 * "address":"xxxx",
 * "province":"四川省",
 * "city":"成都市",
 * "district":"锦江区"
 *
 * @author hp
 */
@Data
public class XbbAddressModel {
    private String address;
    private String province;
    private String city;
    private String district;
}
