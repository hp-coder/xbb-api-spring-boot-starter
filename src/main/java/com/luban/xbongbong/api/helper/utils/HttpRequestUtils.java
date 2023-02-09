package com.luban.xbongbong.api.helper.utils;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http请求工具类
 */
public class HttpRequestUtils {

    /**
     * 发起POST请求
     *
     * @param url   请求url
     * @param param 请求参数(JSON格式)
     * @param sign  签名
     * @return 请求回参
     */
    public static String post(String url, String param, String sign) {
        String body = null;
        // 创建默认的httpClient实例.
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            // 创建http post
            HttpPost httppost = new HttpPost(url);
            httppost.addHeader("sign", sign);
            //参数设置
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(10000)
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .build();
            httppost.setConfig(requestConfig);
            StringEntity uefEntity = new StringEntity(param, "UTF-8");
            uefEntity.setContentType("application/json");
            httppost.setEntity(uefEntity);
            try (CloseableHttpResponse response = httpclient.execute(httppost)) {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    body = EntityUtils.toString(entity, "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return body;
    }
}
