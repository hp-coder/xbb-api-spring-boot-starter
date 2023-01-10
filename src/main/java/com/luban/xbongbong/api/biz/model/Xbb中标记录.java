package com.luban.xbongbong.api.biz.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @author HP 2023/1/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Xbb中标记录 {

    /**
     * 项目名称*
     */
    @NonNull
    @JSONField(name = "text_1")
    private String title;
    /**
     * 中标成交价/下浮率/费率*
     */
    @JSONField(name = "text_2")
    private String price;

    /**
     * 时间*
     */
    @JSONField(name = "text_3")
    private String date;

    /**
     * 项目经理/总监 *
     */
    @JSONField(name = "text_4")
    private String manager;
    /**
     * 业绩类型*
     */
    @NonNull
    @JSONField(name = "text_5")
    private String type;
    /**
     * 项目属地*
     */
    @JSONField(name = "text_6")
    private String area;
    /**
     * 链接*
     */
    @NonNull
    @JSONField(name = "text_9")
    private String link;
    /**
     * 关联值（销帮帮用户id）*
     */
    @NonNull
    @JSONField(
            name = "text_8",
            serialzeFeatures = {
                    SerializerFeature.WriteNonStringValueAsString,
                    SerializerFeature.WriteNonStringKeyAsString
            }
    )
    private Long dataId;


    public Xbb中标记录(@NonNull String title, @NonNull String type, @NonNull String link, @NonNull Long dataId) {
        this.title = title;
        this.type = type;
        this.link = link;
        this.dataId = dataId;
    }
}
