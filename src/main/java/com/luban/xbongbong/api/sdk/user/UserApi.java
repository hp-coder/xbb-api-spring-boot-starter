package com.luban.xbongbong.api.sdk.user;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.config.ConfigConstant;

import java.util.List;

/**
 * @author HP 2022/12/28
 */
public class UserApi {

    /**
     * 系统用户列表*
     * @param nameLike 员工姓名模糊查询
     * @param userIdIn 根据userId集合筛选
     * @param delIgnore 是否查询已离职员工,0-不查询,1-查询,默认不查询
     * @param page 页码，默认为1
     * @param pageSize 每页数量，默认20，最大值100
     * @return
     * @throws Exception
     */
    public static JSONArray list(String nameLike, List<String> userIdIn, boolean delIgnore, int page, int pageSize) throws Exception {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("nameLike", nameLike);
        data.put("userIdIn", userIdIn);
        data.put("delIgnore", delIgnore ? 1 : 0);
        data.put("page", page);
        data.put("pageSize", pageSize);
        data.put("corpid", ConfigConstant.CORP_ID);
        data.put("userId", ConfigConstant.USER_ID);
        //调用xbbApi方法，发起接口请求
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER_LIST, data);
        //对返回值进行解析
        JSONObject responseJson;
        try {
            responseJson = JSON.parseObject(response);
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        JSONArray retArray = null;
        if (responseJson.containsKey("code") && responseJson.getInteger("code").equals(1)) {
            JSONObject result = responseJson.getJSONObject("result");
            if (result != null) {
                retArray = result.getJSONArray("userList");
            }
            return retArray;
        } else {
            throw new Exception(responseJson.getString("msg"));
        }
    }
}
