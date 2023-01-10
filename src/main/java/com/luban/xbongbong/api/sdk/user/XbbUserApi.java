package com.luban.xbongbong.api.sdk.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.model.DingTalkUser;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.user.XbbUserListResponse;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author HP 2022/12/28
 */
public class XbbUserApi {

    /**
     * 系统用户列表*
     *
     * @param nameLike  员工姓名模糊查询
     * @param userIdIn  根据userId集合筛选
     * @param delIgnore 是否查询已离职员工,0-不查询,1-查询,默认不查询
     * @param page      页码，默认为1
     * @param pageSize  每页数量，默认20，最大值100
     * @return
     * @throws Exception
     */
    public static List<DingTalkUser> list(String nameLike, List<String> userIdIn, Boolean delIgnore, Integer page, Integer pageSize) throws Exception {
        //创建参数data
        JSONObject data = new JSONObject();
        Optional.ofNullable(delIgnore).ifPresent(_0 -> data.put("delIgnore", _0 ? 1 : 0));
        Optional.ofNullable(nameLike).ifPresent(_0 -> data.put("nameLike", _0));
        Optional.ofNullable(userIdIn).ifPresent(_0 -> data.put("userIdIn", _0));
        Optional.ofNullable(page).ifPresent(_0 -> data.put("page", _0));
        Optional.ofNullable(pageSize).ifPresent(_0 -> data.put("pageSize", _0));
        //调用xbbApi方法，发起接口请求
        String response = ConfigConstant.xbbApi(ConfigConstant.USER.LIST, data);
        //对返回值进行解析
        XbbResponse<XbbUserListResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbUserListResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbUserListResponse result = xbbResponse.getResult();
            if (result == null) {
                return Collections.emptyList();
            }
            return result.getUserList();
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }

    public static Optional<DingTalkUser> getUserByName(String name) throws Exception {
        final List<DingTalkUser> list = list(name, null, true, 1, 1);
        if (CollectionUtils.isEmpty(list)) {
            return Optional.empty();
        }
        return Optional.ofNullable(list.get(0));
    }


}
