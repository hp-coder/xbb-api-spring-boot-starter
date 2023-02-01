package com.luban.xbongbong.api.sdk.customer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.XbbFormConditionSymbol;
import com.luban.xbongbong.api.helper.enums.XbbSubBusinessType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.customer.*;
import lombok.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 客户模块
 */
public class XbbCustomerApi {

    private XbbCustomerApi(){
        throw new AssertionError();
    }

    /**
     * 客户信息详情接口*
     *
     * @param dataId 客户数据id
     * @return 客户数据，具体字段参考 resources/detail.json
     * @throws Exception
     */
    public static XbbCustomerDetailResponse get(@NonNull Long dataId) throws Exception {
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.GET, data);
        XbbResponse<XbbCustomerDetailResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomerDetailResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return xbbResponse.getResult();
        } else {
            throw new Exception(xbbResponse.getMsg());
        }
    }

    /**
     * 通过系统的用户ID来查询 *
     *
     * @param userId
     * @return 客户数据，具体字段参考 resources/detail.json
     * @throws Exception
     */
    public static XbbCustomerDetailResponse getByUserId(@NonNull Long formId, @NonNull String userIdField, @NonNull Long userId) throws Exception {
        final XbbFormCondition xbbFormCondition = new XbbFormCondition();
        xbbFormCondition.setAttr(userIdField);
        xbbFormCondition.setSymbol(XbbFormConditionSymbol.eq);
        xbbFormCondition.setValue(Collections.singletonList(userId));
        final XbbCustomerListResponse response = list(formId, Collections.singletonList(xbbFormCondition), null, null, 1, 1);
        if (response == null || CollectionUtils.isEmpty(response.getList())) {
            return null;
        }
        return get(response.getList().get(0).getDataId());
    }

    /**
     * 客户列表接口*
     * @param formId 表单id
     * @param conditions 查询条件
     * @param isPublic 是否公海
     * @param del 是否删除（回收站）
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 用户数据列表响应
     * @throws Exception 请求异常
     */
    public static XbbCustomerListResponse list(@NonNull Long formId, List<XbbFormCondition> conditions, Boolean isPublic, Boolean del, int page, Integer pageSize) throws Exception {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("page", page);
        Optional.ofNullable(conditions).ifPresent(_0 -> data.put("conditions", conditions));
        Optional.ofNullable(pageSize).ifPresent(_0 -> data.put("pageSize", _0));
        Optional.ofNullable(isPublic).ifPresent(_0 -> data.put("isPublic", isPublic ? 1 : 0));
        Optional.ofNullable(del).ifPresent(_0 -> data.put("del", del ? 1 : 0));
        //调用xbbApi方法，发起接口请求
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.LIST, data);
        //对返回值进行解析
        XbbResponse<XbbCustomerListResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomerListResponse>>() {
            });
        } catch (Exception e) {
            throw new Exception("json解析出错", e);
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            return xbbResponse.getResult();
        } else {
            throw new Exception(xbbResponse.getMsg());
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
        data.put("dataList", dataList);
        //调用xbbApi方法，发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.ADD, data);
        //对返回值进行解析
        return getCustomerAlterDataId(response);
    }

    public static Long edit(@NonNull Long formId, @NonNull Long dataId, @NonNull JSONObject dataList) throws XbbException {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataId", dataId);
        data.put("dataList", dataList);
        //调用xbbApi方法，发起API请求
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.EDIT, data);
        //对返回值进行解析
        return getCustomerAlterDataId(response);
    }

    private static Long getCustomerAlterDataId(String response) {
        XbbResponse<XbbCustomerAlterResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomerAlterResponse>>() {
            });
        } catch (Exception e) {
            throw new XbbException(-1, "json解析出错");
        }
        Long dataId = null;
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbCustomerAlterResponse alterResponse = xbbResponse.getResult();
            if (alterResponse != null) {
                dataId = alterResponse.getDataId();
            }
            return dataId;
        } else {
            throw new XbbException(xbbResponse.getCode(), xbbResponse.getMsg());
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
        data.put("dataIdList", dataIds);
        data.put("businessUserId", managerId);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.DISTRIBUTION, data);
        //对返回值进行解析
        return booleanResponse(response);
    }

    /**
     * 客户退回到公海池
     * 等于删除所有负责人
     *
     * @param dataIds 客户id集合
     * @return 是否成功
     */
    public static boolean backToPublicSea(@NonNull List<Long> dataIds) {
        JSONObject data = new JSONObject();
        data.put("dataIdList", dataIds);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.BACK_TO_PUBLIC_SEA, data);
        //对返回值进行解析
        return booleanResponse(response);
    }

    /**
     * 删除客户负责人*
     *
     * @param dataId          客户id
     * @param dingTalkUserIds 钉钉用户id
     * @return 是否成功
     */
    public static boolean removeOwner(@NonNull Long dataId, @NonNull List<String> dingTalkUserIds) {
        JSONObject data = new JSONObject();
        data.put("businessUserIdList", dingTalkUserIds);
        data.put("dataId", dataId);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.OWNER_REMOVE, data);
        //对返回值进行解析
        return booleanResponse(response);
    }


    /**
     * 客户移交
     * 就是将负责人变更*
     *
     * @param dingTalkUserId 钉钉用户id = 销帮帮用户id
     * @param dataIds        客户id集合
     * @return 是否成功
     */
    public static boolean handover(@NonNull String dingTalkUserId, @NonNull List<Long> dataIds) {
        JSONObject data = new JSONObject();
        data.put("businessUserId", dingTalkUserId);
        data.put("dataIdList", dataIds);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.HANDOVER, data);
        //对返回值进行解析
        return booleanResponse(response);
    }

    /**
     * 将客户删除到回收站中*
     *
     * @param dataId 客户id
     * @return 是否成功
     */
    public static boolean delete(@NonNull Long dataId) {
        JSONObject data = new JSONObject();
        data.put("dataId", dataId);
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.DELETE, data);
        //对返回值进行解析
        XbbResponse<XbbCustomerDeleteResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomerDeleteResponse>>() {
            });
        } catch (Exception e) {
            throw new XbbException(-1, "json解析出错");
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbCustomerDeleteResponse result = xbbResponse.getResult();
            if (result != null) {
                Assert.isTrue(Objects.equals("", result.getErrorDataMemo()), () -> {
                    throw new XbbException(-1, JSONObject.toJSONString(result));
                });
                return true;
            }
            return false;
        } else {
            throw new XbbException(xbbResponse.getCode(), xbbResponse.getMsg());
        }
    }


    private static boolean booleanResponse(String response) {
        XbbResponse<XbbCustomerCommonResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<XbbResponse<XbbCustomerCommonResponse>>() {
            });
        } catch (Exception e) {
            throw new XbbException(-1, "json解析出错");
        }
        if (Objects.equals(xbbResponse.getCode(), 1)) {
            final XbbCustomerCommonResponse result = xbbResponse.getResult();
            if (result != null) {
                Assert.isTrue(Objects.equals("success", result.getResultType()), () -> {
                    throw new XbbException(-1, JSON.toJSONString(result));
                });
                return true;
            }
            return false;
        } else {
            throw new XbbException(xbbResponse.getCode(), xbbResponse.getMsg());
        }
    }
}
