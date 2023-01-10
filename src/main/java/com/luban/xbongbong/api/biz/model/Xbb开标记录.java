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
public class Xbb开标记录 {

    /**
     * 开标记录标题*
     */
    @NonNull
    @JSONField(name = "text_1")
    private String title;

    /**
     * 投标报价(万元)/下浮率/费率*
     */
    @JSONField(name = "text_5")
    private String price;
    /**
     * 发布时间*
     */
    @JSONField(name = "text_3")
    private String date;
    /**
     * 项目属地*
     */
    @JSONField(name = "text_9")
    private String area;
    /**
     * 链接*
     */
    @NonNull
    @JSONField(name = "text_4")
    private String link;
    /**
     * 实际时查询到的用户id*
     */
    @NonNull
    @JSONField(name = "text_6",
            serialzeFeatures = {
                    SerializerFeature.WriteNonStringValueAsString,
                    SerializerFeature.WriteNonStringKeyAsString
            }
    )
    private Long dataId;

    public Xbb开标记录(@NonNull String title, @NonNull String link, @NonNull Long dataId) {
        this.title = title;
        this.link = link;
        this.dataId = dataId;
    }
}
