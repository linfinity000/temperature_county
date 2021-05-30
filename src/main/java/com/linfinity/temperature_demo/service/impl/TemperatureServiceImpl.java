package com.linfinity.temperature_demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.linfinity.temperature_demo.dto.SimilaryInfo;
import com.linfinity.temperature_demo.dto.WeatherInfo;
import com.linfinity.temperature_demo.helper.HttpClientUtil;
import com.linfinity.temperature_demo.helper.RemoteUtil;
import com.linfinity.temperature_demo.helper.SimilarDegreeHelper;
import com.linfinity.temperature_demo.service.TemperatureServiceI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;

@Service("temperatureServiceI")
public class TemperatureServiceImpl implements TemperatureServiceI {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 信号量--设置为100
    public static final Semaphore semaphore = new Semaphore(100);

    /**
     * ERROR_MESSAGE
     */
    public static final String ERROR_MESSAGE = "您访问的页面不存在";


    /**
     * key word
     */
    public static final String WEATHER_INFO = "weatherinfo";
    /**
     * Invalid region code return value
     */
    public static final int INVALID_CODE = 0;

    @Value("${REST.PRE_URL}")
    private String PRE_URL;

    @Value("${REST.SUFF_URL}")
    private String SUFF_URL;

    @Value("${REST.ALL_PROVINCE}")
    private String ALL_PROVINCE;

    @Value("${REST.ASSIGN_CITY}")
    private String ASSIGN_CITY;

    @Value("${REST.ASSIGN_COUNTY}")
    private String ASSIGN_COUNTY;


    @Autowired
    private RemoteUtil remoteUtil;

    @Override
    public Optional<Integer> getTemperatureByLocalCode(String province, String city, String county){
        Optional<Integer> op = null;
        try {
            Assert.isTrue(StringUtils.isNotBlank(province),"省编码为空，请检查");
            Assert.isTrue(StringUtils.isNotBlank(city),"市编码为空，请检查");
            Assert.isTrue(StringUtils.isNotBlank(county),"区编码为空，请检查");
            semaphore.acquire();
            WeatherInfo weatherInfo = this.parseWeatherInfo(province,city,county);
            op = Optional.ofNullable(
                        Double.valueOf(weatherInfo.getTemp())
                                .intValue());
        }catch (Exception e){
            e.printStackTrace();
            logger.error("调用天气接口异常：{}",e.getMessage());
            op = Optional.ofNullable(0);
        }finally {
            semaphore.release();
            return op;
        }
    }

    /**
     * 获取天气对象
     * @param province
     * @param city
     * @param county
     * @return
     * @throws Exception
     */
    private WeatherInfo parseWeatherInfo(String province, String city, String county) throws Exception{
        WeatherInfo weatherInfo = null;
        StringBuffer url = new StringBuffer(PRE_URL)
                .append(province)
                .append(city)
                .append(county)
                .append(SUFF_URL);
        String result = remoteUtil.invokeTemperatureApi(url.toString());
        if (result.contains(ERROR_MESSAGE)) {
           // 使用相似度算法再次匹配
            String new_province = this.matchSimilaryCode(province,ALL_PROVINCE);
            String new_city = this.matchSimilaryCode(city,
                    new StringBuffer(ASSIGN_CITY).append(new_province).append(SUFF_URL).toString());
            String new_county = this.matchSimilaryCode(city,
                    new StringBuffer(ASSIGN_COUNTY).append(new_province)
                    .append(new_city).append(SUFF_URL).toString());
           return parseWeatherInfo(new_province,new_city,new_county);
        }
        JSONObject jb = JSONObject.parseObject(result);
        weatherInfo = JSONObject.parseObject(jb.get(WEATHER_INFO).toString(),WeatherInfo.class);
        return weatherInfo;
    }

    /**
     * 匹配相似地区编码
     * @param originalCode      原始地区编码
     * @return
     */
    private String matchSimilaryCode(String originalCode,String url) {
        String result = HttpClientUtil.sendGet(url);
        Map<String,String> map = JSON.parseObject(result,Map.class);
        List<SimilaryInfo> lists = new ArrayList<>();
        if (!map.isEmpty()) {
            map.keySet().stream().forEach(item -> {
              double score =  SimilarDegreeHelper.getSimilarDegree(originalCode, item);
                lists.add(new SimilaryInfo(score,item));
            });
        }
        // 取出得分最大的一个对象
        Optional<SimilaryInfo> codes = lists.stream().collect(Collectors.maxBy(Comparator.comparing(SimilaryInfo::getScore)));
        // 匹配相似度最高的省
        return codes.get().getCode();
    }
}
