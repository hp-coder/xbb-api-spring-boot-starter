package com.luban.xbongbong.api.service.contract;

import com.luban.xbongbong.api.helper.XbbUrl;
import com.luban.xbongbong.api.helper.utils.XbbApiCaller;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import lombok.NonNull;

/**
 * @author hp
 */
public class XbbContractApi {

    private XbbContractApi() {
        throw new AssertionError();
    }

    public static XbbDetailModel get(@NonNull Long dataId) throws Exception {
        return XbbApiCaller.get(dataId, XbbUrl.Contract.GET);
    }
}
