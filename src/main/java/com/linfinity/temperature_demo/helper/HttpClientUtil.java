package com.linfinity.temperature_demo.helper;

import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http请求工具
 */
public class HttpClientUtil {

    public static String sendGet(String url) {
        // 设置请求的参数
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(3000).setConnectTimeout(3000)
                .setSocketTimeout(3000).build();
        String respContent = null;
        //创建 CloseableHttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            // 请求发送成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                respContent = EntityUtils.toString(response.getEntity(),"UTF-8");
            } else {
                // 上游调用，捕获异常重试
                throw new RuntimeException("调用第三方接口异常");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (httpClient != null) {
                try {
                    httpClient.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return respContent;
    }
}
