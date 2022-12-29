package com.luban.xbongbong.api.sdk.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.XbbSubBusinessType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import lombok.NonNull;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;

/**
 * 客户模块
 */
public class XbbCustomerApi {

    /**
     * 客户信息详情接口*
     *
     * @param dataId 客户数据id
     * @return 客户数据，具体字段参考 resources/detail.json
     * @throws Exception
     */
    public static JSONObject get(@NonNull Long dataId) throws Exception {
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        data.put("corpid", ConfigConstant.CORP_ID);
        data.put("userId", ConfigConstant.USER_ID);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER_GET, data);
        JSONObject responseJson;
        try {
            responseJson = JSON.parseObject(response);
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (responseJson.containsKey("code") && responseJson.getInteger("code").equals(1)) {
            return responseJson.getJSONObject("result");
        } else {
            throw new Exception(responseJson.getString("msg"));
        }
    }

    /**
     * 客户列表接口
     *
     * @param formId   表单id
     * @param page     页码
     * @param pageSize 每页数量
     * @return 接口回参-客户列表
     * @throws Exception 异常
     */
    public static JSONArray list(@NonNull Long formId, int page, int pageSize) throws Exception {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("del", 0);
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
                retArray = result.getJSONArray("list");
            }
            return retArray;
        } else {
            throw new Exception(responseJson.getString("msg"));
        }
    }

    /**
     * 新建客户接口
     *
     * @param formId   表单id
     * @param dataList 表单数据
     * @return 接口回参
     * @throws XbbException 异常
     */
    public static Long add(@NonNull Long formId, @NonNull JSONObject dataList) throws XbbException {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("corpid", ConfigConstant.CORP_ID);
        data.put("userId", ConfigConstant.USER_ID);
        data.put("dataList", dataList);
        //调用xbbApi方法，发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER_ADD, data);
        //对返回值进行解析
        JSONObject responseJson;
        try {
            responseJson = JSON.parseObject(response);
        } catch (Exception e) {
            throw new XbbException(-1, "json解析出错");
        }
        Long dataId = null;
        if (responseJson.containsKey("code") && responseJson.getInteger("code").equals(1)) {
            JSONObject result = responseJson.getJSONObject("result");
            if (result != null) {
                dataId = result.getLong("formDataId");
            }
            return dataId;
        } else {
            throw new XbbException(responseJson.getInteger("code"), responseJson.getString("msg"));
        }
    }

    /**
     * 分配客户接口*
     *
     * @param managerId       客户经理钉钉userid
     * @param dataIds         客户id
     * @param subBusinessType 子业务类型，是否公海用户，一般情况传 XbbSubBusinessType.DISTRIBUTED_CUSTOMER
     * @return 是否成功
     * @throws XbbException 异常
     */
    public static boolean distribution(@NonNull String managerId, @NonNull List<Long> dataIds, @NonNull XbbSubBusinessType subBusinessType) throws XbbException {
        JSONObject data = new JSONObject();
        data.put("subBusinessType", subBusinessType.getCode());
        data.put("corpid", ConfigConstant.CORP_ID);
        data.put("userId", ConfigConstant.USER_ID);
        data.put("dataIdList", dataIds);
        data.put("businessUserId", managerId);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER_DISTRIBUTION, data);
        //对返回值进行解析
        JSONObject responseJson;
        try {
            responseJson = JSON.parseObject(response);
        } catch (Exception e) {
            throw new XbbException(-1, "json解析出错");
        }
        if (responseJson.containsKey("code") && responseJson.getInteger("code").equals(1)) {
            JSONObject result = responseJson.getJSONObject("result");
            if (result != null) {
                Assert.isTrue(Objects.equals("success", result.getString("resultType")), () -> {
                    throw new XbbException(-1, result.toJSONString());
                });
                return true;
            }
            return false;
        } else {
            throw new XbbException(responseJson.getInteger("code"), responseJson.getString("msg"));
        }
    }
}
