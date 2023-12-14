package com.luban.xbongbong.api.helper;

import com.luban.common.base.annotations.FieldDesc;
import com.luban.xbongbong.api.helper.enums.api.ApiType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.luban.xbongbong.api.helper.enums.api.ApiType.READ;
import static com.luban.xbongbong.api.helper.enums.api.ApiType.WRITE;

/**
 * @author hp
 */
public interface XbbUrl {
    String getUri();

    ApiType getType();

    @Getter
    @AllArgsConstructor
    enum PaymentSheet implements XbbUrl {
        @FieldDesc("回款单列表")
        LIST("/paymentSheet/list", READ),
        @FieldDesc("回款单详情")
        GET("/paymentSheet/list", READ);

        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Form implements XbbUrl {
        @FieldDesc("表单模板列表")
        LIST("/form/list", READ),
        @FieldDesc("表单模板字段解释")

        GET("/form/get", READ);;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Label implements XbbUrl {
        @FieldDesc("批量添加标签")
        BATCH_ADD("/label/batch/add", WRITE),
        @FieldDesc("表单标签组")
        LIST("/label/allList", READ),
        @FieldDesc("移除标签")
        REMOVE("/label/batch/remove", WRITE),
        ;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Customer implements XbbUrl {
        @FieldDesc("客户列表接口")
        LIST("/customer/list", READ),
        @FieldDesc("客户新建接口")
        ADD("/customer/add", WRITE),
        @FieldDesc("客户分配接口")
        DISTRIBUTION("/customer/distribution", WRITE),
        @FieldDesc("客户详情")
        GET("/customer/detail", READ),
        @FieldDesc("客户退回公海池")
        BACK_TO_PUBLIC_SEA("/customer/back", WRITE),
        @FieldDesc("删除客户负责人")
        OWNER_REMOVE("/customer/deleteMainUser", WRITE),
        @FieldDesc("客户移交")
        HANDOVER("/customer/handover", WRITE),
        @FieldDesc("删除客户")
        DELETE("/customer/del", WRITE),
        @FieldDesc("编辑用户")
        EDIT("/customer/edit", WRITE),
        ;

        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum User implements XbbUrl {
        @FieldDesc("用户列表")
        LIST("/user/list", READ),
        ;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum CustomForm implements XbbUrl {
        @FieldDesc("添加自定义表单的数据")
        ADD("/paas/add", WRITE),
        @FieldDesc("删除自定义表单的数据")
        DELETE("/paas/del", WRITE),
        @FieldDesc("自定义表单的数据列表")
        LIST("/paas/list", READ),
        @FieldDesc("编辑自定义表单的数据")
        EDIT("/paas/edit", WRITE),
        @FieldDesc("自定义表单的数据详情")
        GET("/paas/detail", READ),
        ;
        private final String uri;
        private final ApiType type;
    }

    @Getter
    @AllArgsConstructor
    enum Contract implements XbbUrl {
        @FieldDesc("合同订单详情")
        GET("/contract/detail", READ),
        ;
        private final String uri;
        private final ApiType type;
    }
}
