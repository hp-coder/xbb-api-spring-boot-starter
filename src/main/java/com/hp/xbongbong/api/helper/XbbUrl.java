package com.hp.xbongbong.api.helper;

import com.google.common.base.Preconditions;
import com.hp.xbongbong.api.helper.enums.api.ApiType;
import com.luban.common.base.annotations.FieldDesc;
import com.hp.xbongbong.api.XbbProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author hp
 */
public interface XbbUrl {
    String getUri();

    ApiType getType();

    default String getRequestUrl() {
        final String apiGateway = XbbProperties.API_GATEWAY;
        Preconditions.checkArgument(Objects.nonNull(apiGateway));
        return apiGateway + getUri();
    }

    default String getLogInfo() {
        return "type=" + getType().name()+"||url=" + getRequestUrl();
    }

    @Getter
    @AllArgsConstructor
    enum PaymentSheet implements XbbUrl {
        @FieldDesc("回款单列表")
        LIST("/paymentSheet/list", ApiType.READ),
        @FieldDesc("回款单详情")
        GET("/paymentSheet/detail", ApiType.READ);

        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Form implements XbbUrl {
        @FieldDesc("表单模板列表")
        LIST("/form/list", ApiType.READ),
        @FieldDesc("表单模板字段解释")

        GET("/form/get", ApiType.READ);;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Label implements XbbUrl {
        @FieldDesc("批量添加标签")
        BATCH_ADD("/label/batch/add", ApiType.WRITE),
        @FieldDesc("表单标签组")
        LIST("/label/allList", ApiType.READ),
        @FieldDesc("移除标签")
        REMOVE("/label/batch/remove", ApiType.WRITE),
        ;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Customer implements XbbUrl {
        @FieldDesc("客户列表接口")
        LIST("/customer/list", ApiType.READ),
        @FieldDesc("客户新建接口")
        ADD("/customer/add", ApiType.WRITE),
        @FieldDesc("客户分配接口")
        DISTRIBUTION("/customer/distribution", ApiType.WRITE),
        @FieldDesc("客户详情")
        GET("/customer/detail", ApiType.READ),
        @FieldDesc("客户退回公海池")
        BACK_TO_PUBLIC_SEA("/customer/back", ApiType.WRITE),
        @FieldDesc("删除客户负责人")
        OWNER_REMOVE("/customer/deleteMainUser", ApiType.WRITE),
        @FieldDesc("客户移交")
        HANDOVER("/customer/handover", ApiType.WRITE),
        @FieldDesc("删除客户")
        DELETE("/customer/del", ApiType.WRITE),
        @FieldDesc("编辑用户")
        EDIT("/customer/edit", ApiType.WRITE),
        ;

        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum User implements XbbUrl {
        @FieldDesc("用户列表")
        LIST("/user/list", ApiType.READ),
        ;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum CustomForm implements XbbUrl {
        @FieldDesc("添加自定义表单的数据")
        ADD("/paas/add", ApiType.WRITE),
        @FieldDesc("删除自定义表单的数据")
        DELETE("/paas/del", ApiType.WRITE),
        @FieldDesc("自定义表单的数据列表")
        LIST("/paas/list", ApiType.READ),
        @FieldDesc("编辑自定义表单的数据")
        EDIT("/paas/edit", ApiType.WRITE),
        @FieldDesc("自定义表单的数据详情")
        GET("/paas/detail", ApiType.READ),
        ;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Contract implements XbbUrl {
        @FieldDesc("合同订单详情")
        GET("/contract/detail", ApiType.READ),
        ;
        private final String uri;
        private final ApiType type;
    }
}
