package com.hp.xbongbong.api.service.user;

import com.alibaba.fastjson.JSONObject;
import com.hp.xbongbong.api.helper.XbbUrl;
import com.hp.xbongbong.api.helper.utils.XbbApiCaller;
import com.hp.xbongbong.api.model.DingTalkUser;
import com.hp.xbongbong.api.model.user.XbbUserListResponse;
import com.hp.xbongbong.api.model.XbbResponse;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author HP 2022/12/28
 */
public class XbbUserApi {

    private XbbUserApi() {
        throw new AssertionError();
    }

    /**
     * 系统用户列表*
     *
     * @param nameLike  员工姓名模糊查询
     * @param userIdIn  根据userId集合筛选
     * @param delIgnore 是否查询已离职员工,0-不查询,1-查询,默认不查询
     * @param page      页码，默认为1
     * @param pageSize  每页数量，默认20，最大值100
     */
    public static List<DingTalkUser> list(
            String nameLike,
            List<String> userIdIn,
            Boolean delIgnore,
            Integer page,
            Integer pageSize
    ) {
        JSONObject data = new JSONObject();
        Optional.ofNullable(delIgnore).ifPresent(i -> data.put("delIgnore", i ? 1 : 0));
        Optional.ofNullable(nameLike).ifPresent(i -> data.put("nameLike", i));
        Optional.ofNullable(userIdIn).ifPresent(i -> data.put("userIdIn", i));
        Optional.ofNullable(page).ifPresent(i -> data.put("page", i));
        Optional.ofNullable(pageSize).ifPresent(i -> data.put("pageSize", i));

        final XbbResponse<XbbUserListResponse> response = XbbApiCaller.call(XbbUrl.User.LIST, data);
        if (response.isNullResult()) {
            return Collections.emptyList();
        }
        return response.getResult(XbbUserListResponse.class).getUserList();
    }

    public static Optional<DingTalkUser> getUserByName(String name) {
        final List<DingTalkUser> list = list(name, null, true, 1, 1);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }
}
