package com.luban.xbongbong.api.sdk.contract;

import com.luban.xbongbong.api.helper.config.ConfigConstant;
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
        return ConfigConstant.get(dataId, ConfigConstant.CONTRACT.GET);
    }
}
