package com.luban.xbongbong.api.service.customer;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSONObject;
import com.luban.xbongbong.api.helper.enums.XbbFormConditionSymbol;
import com.luban.xbongbong.api.helper.enums.XbbSubBusinessType;
import com.luban.xbongbong.api.helper.exception.XbbException;
import com.luban.xbongbong.api.helper.utils.XbbApiCaller;
import com.luban.xbongbong.api.model.XbbFormCondition;
import com.luban.xbongbong.api.model.XbbResponse;
import com.luban.xbongbong.api.model.common.detail.XbbDetailModel;
import com.luban.xbongbong.api.model.common.list.XbbListItemModel;
import com.luban.xbongbong.api.model.common.page.XbbPageResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerAlterResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerCommonResponse;
import com.luban.xbongbong.api.model.customer.XbbCustomerDeleteResponse;
import lombok.NonNull;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.luban.xbongbong.api.helper.XbbUrl.Customer;

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


    public static List<XbbListItemModel> recursivelyGetCustomersByConditions(List<XbbFormCondition> conditions) {
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
    public static XbbDetailModel get(@NonNull Long dataId) {
        return XbbApiCaller.get(dataId, Customer.GET);
    }

    /**
     * 通过系统的用户ID来查询 *
     *
     * @return 客户数据，具体字段参考 resources/detail.json
     */
    public static XbbDetailModel getByUserId(@NonNull Long formId, @NonNull String userIdField, @NonNull Long userId) {
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
     */
    public static XbbPageResponse list(@NonNull Long formId, List<XbbFormCondition> conditions, Boolean isPublic, Boolean del, int page, Integer pageSize) {
        final JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("page", page);
        Optional.ofNullable(conditions).ifPresent(i -> data.put("conditions", conditions));
        Optional.ofNullable(pageSize).ifPresent(i -> data.put("pageSize", i));
        Optional.ofNullable(isPublic).ifPresent(i -> data.put("isPublic", isPublic ? 1 : 0));
        Optional.ofNullable(del).ifPresent(i -> data.put("del", del ? 1 : 0));

        final XbbResponse<XbbPageResponse> response = XbbApiCaller.call(Customer.LIST, data);
        return response.getResult(XbbPageResponse.class);
    }

    /**
     * 新建客户接口
     *
     * @param formId   表单id
     * @param dataList 表单数据
     * @return 接口回参
     */
    public static Long add(@NonNull Long formId, @NonNull JSONObject dataList) {
        JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataList", dataList);

        final XbbResponse<XbbCustomerAlterResponse> response = XbbApiCaller.call(Customer.ADD, data);
        return getCustomerAlterDataId(response);
    }

    public static Long edit(@NonNull Long formId, @NonNull Long dataId, @NonNull JSONObject dataList) throws XbbException {
        final JSONObject data = new JSONObject();
        data.put("formId", formId);
        data.put("dataId", dataId);
        data.put("dataList", dataList);

        final XbbResponse<XbbCustomerAlterResponse> response = XbbApiCaller.call(Customer.EDIT, data);
        return getCustomerAlterDataId(response);
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
    public static boolean distribution(@NonNull String managerId, @NonNull List<Long> dataIds, @NonNull XbbSubBusinessType subBusinessType) {
        final JSONObject data = new JSONObject();
        data.put("subBusinessType", subBusinessType.getCode());
        data.put("dataIdList", dataIds);
        data.put("businessUserId", managerId);

        final XbbResponse<XbbCustomerCommonResponse> response = XbbApiCaller.call(Customer.DISTRIBUTION, data);
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
        final JSONObject data = new JSONObject();
        data.put("dataIdList", dataIds);

        final XbbResponse<XbbCustomerCommonResponse> response = XbbApiCaller.call(Customer.BACK_TO_PUBLIC_SEA, data);
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
        final JSONObject data = new JSONObject();
        data.put("businessUserIdList", dingTalkUserIds);
        data.put("dataId", dataId);

        final XbbResponse<XbbCustomerCommonResponse> response = XbbApiCaller.call(Customer.OWNER_REMOVE, data);
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
        final JSONObject data = new JSONObject();
        data.put("businessUserId", dingTalkUserId);
        data.put("dataIdList", dataIds);

        final XbbResponse<XbbCustomerCommonResponse> response = XbbApiCaller.call(Customer.HANDOVER, data);
        return booleanResponse(response);
    }

    /**
     * 将客户删除到回收站中*
     *
     * @param dataId 客户id
     * @return 是否成功
     */
    public static boolean delete(@NonNull Long dataId) {
        final JSONObject data = new JSONObject();
        data.put("dataId", dataId);

        final XbbResponse<XbbCustomerDeleteResponse> response = XbbApiCaller.call(Customer.DELETE, data);
        if (response.isNullResult()) {
            return false;
        }
        return response.getResult(XbbCustomerDeleteResponse.class).succeed();
    }

    private static Long getCustomerAlterDataId(XbbResponse<XbbCustomerAlterResponse> response) {
        if (response.isNullResult()) {
            return null;
        }
        return response.getResult(XbbCustomerAlterResponse.class).getDataId();
    }

    private static boolean booleanResponse(XbbResponse<XbbCustomerCommonResponse> response) {
        final XbbCustomerCommonResponse result = response.getResult(XbbCustomerCommonResponse.class);
        if (response.isNullResult()) {
            return false;
        }
        return result.succeed();
    }
}
