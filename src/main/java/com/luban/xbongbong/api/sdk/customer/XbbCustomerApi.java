package com.luban.xbongbong.api.sdk.customer;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.luban.xbongbong.api.helper.config.ConfigConstant;
import com.luban.xbongbong.api.helper.enums.XbbFormConditionSymbol;
import com.luban.xbongbong.api.helper.enums.XbbSubBusinessType;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.common.list.XbbListItemModel;
import com.luban.xbongbong.api.model.common.page.XbbPageResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerAlterResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerCommonResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerDeleteResponse;
import lombok.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * 客户模块
 *
 * @author hp
 */
public class XbbCustomerApi {

    private static final Long CUSTOMER_FORM_ID = 7174754L;

    private XbbCustomerApi() {
        throw new AssertionError();
    }


    public static List<XbbListItemModel> recursivelyGetCustomersByConditions(List<XbbFormCondition> conditions) throws Exception {
        List<XbbListItemModel> total = new ArrayList<>();
        List<XbbListItemModel> list = null;
        int page = 1;
        do {
            final XbbPageResponse response = XbbCustomerApi.list(CUSTOMER_FORM_ID, conditions, null, null, page++, 100);
            if (response != null) {
                list = response.getList();
                total.addAll(list);
            }
        } while (CollUtil.isNotEmpty(list));
        return total;
    }

    /**
     * 客户信息详情接口*
     *
     * @param dataId 客户数据id
     * @return 客户数据，具体字段参考 resources/detail.json
     */
    public static XbbDetailModel get(@NonNull Long dataId) throws Exception {
        return ConfigConstant.get(dataId, ConfigConstant.CUSTOMER.GET);
    }

    /**
     * 通过系统的用户ID来查询 *
     *
     * @return 客户数据，具体字段参考 resources/detail.json
     */
    public static XbbDetailModel getByUserId(@NonNull Long formId, @NonNull String userIdField, @NonNull Long userId) throws Exception {
        final XbbFormCondition xbbFormCondition = new XbbFormCondition();
        xbbFormCondition.setAttr(userIdField);
        xbbFormCondition.setSymbol(XbbFormConditionSymbol.eq);
        xbbFormCondition.setValue(Collections.singletonList(userId));
        final XbbPageResponse response = list(formId, Collections.singletonList(xbbFormCondition), null, null, 1, 1);
        if (response == null || CollectionUtils.isEmpty(response.getList())) {
            return null;
        }
        return get(response.getList().get(0).getDataId());
    }

    /**
     * 客户列表接口*
     *
     * @param formId     表单id
     * @param conditions 查询条件
     * @param isPublic   是否公海
     * @param del        是否删除（回收站）
     * @param page       页码
     * @param pageSize   每页记录数
     * @return 用户数据列表响应
     * @throws Exception 请求异常
     */
    public static XbbPageResponse list(@NonNull Long formId, List<XbbFormCondition> conditions, Boolean isPublic, Boolean del, int page, Integer pageSize) throws Exception {
        //创建参数data
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("page", page);
        Optional.ofNullable(conditions).ifPresent(i -> data.put("conditions", conditions));
        Optional.ofNullable(pageSize).ifPresent(i -> data.put("pageSize", i));
        Optional.ofNullable(isPublic).ifPresent(i -> data.put("isPublic", isPublic ? 1 : 0));
        Optional.ofNullable(del).ifPresent(i -> data.put("del", del ? 1 : 0));
        //调用xbbApi方法，发起接口请求
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.LIST, data, ApiType.READ);
        //对返回值进行解析
        XbbResponse<XbbPageResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.ADD, data, ApiType.WRITE);
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.EDIT, data, ApiType.WRITE);
        //对返回值进行解析
        return getCustomerAlterDataId(response);
    }

    private static Long getCustomerAlterDataId(String response) {
        XbbResponse<XbbCustomerAlterResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.DISTRIBUTION, data, ApiType.WRITE);
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.BACK_TO_PUBLIC_SEA, data, ApiType.WRITE);
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.OWNER_REMOVE, data, ApiType.WRITE);
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.HANDOVER, data, ApiType.WRITE);
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
        String response = ConfigConstant.xbbApi(ConfigConstant.CUSTOMER.DELETE, data, ApiType.WRITE);
        //对返回值进行解析
        XbbResponse<XbbCustomerDeleteResponse> xbbResponse;
        try {
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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
            xbbResponse = JSON.parseObject(response, new TypeReference<>() {
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
