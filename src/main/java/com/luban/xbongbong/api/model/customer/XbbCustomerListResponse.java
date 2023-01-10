package com.luban.xbongbong.api.model.customer;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.List;

/**
 * @author HP 2022/12/30
 */
@Data
public class XbbCustomerListResponse {
    private List<XbbCustomer> list;
    private Integer totalCount;
    private Integer totalPage;

    @Data
    public static class XbbCustomer {
        private Long addTime;
        private JSONObject data;
        private Long dataId;
        private Long formId;
        private Long updateTime;
    }
}
