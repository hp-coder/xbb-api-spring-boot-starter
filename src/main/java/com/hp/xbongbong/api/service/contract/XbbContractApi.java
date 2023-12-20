package com.hp.xbongbong.api.service.contract;

import com.hp.xbongbong.api.helper.XbbUrl;
import com.hp.xbongbong.api.helper.utils.XbbApiCaller;
import com.hp.xbongbong.api.model.common.detail.XbbDetailModel;
import lombok.NonNull;

/**
 * @author hp
 */
public class XbbContractApi {

    private XbbContractApi() {
        throw new AssertionError();
    }

    public static XbbDetailModel get(@NonNull Long dataId) {
        return XbbApiCaller.get(dataId, XbbUrl.Contract.GET);
    }
}
