package com.linfinity.temperature_demo.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

/**
 * 远程调用接口
 */
@Component
public class RemoteUtil {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 调用第三方天气接口，获取天气结果信息（异常重试）
     * @param url       URL
     * @return
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000, multiplier = 1, maxDelay = 10000))
    public String invokeTemperatureApi(String url) {
        logger.info("----invokeTemperatureApi--------");
        String result = HttpClientUtil.sendGet(url);
        logger.info("---调用接口，返回：{}",result);
        return result;
    }
}
