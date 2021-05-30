package com.linfinity.temperature_demo;

import com.linfinity.temperature_demo.service.TemperatureServiceI;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;

@SpringBootTest
class TemperatureDemoApplicationTests {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TemperatureServiceI temperatureServiceI;

    /**
     * 正常接口调用
     */
    @Test
    public void getTemperature() {
        try {
            Optional<Integer> op = this.temperatureServiceI.getTemperatureByLocalCode("10119","04","01");
            logger.info("获取当前地区的温度：{}",op.get());
        }catch (Exception e){
            logger.error("获取该地区温度异常，{}",e.getMessage());
        }
    }

    /**
     * 获取错误地址匹配的温度（匹配新的地址）
     */
    @Test
    public void getMatchCodeTemperature(){
        try {
            Optional<Integer> op = this.temperatureServiceI.getTemperatureByLocalCode("10119","04","122324");
            logger.info("获取当前地区的温度：{}",op.get());
        }catch (Exception e){
            logger.error("获取该地区温度异常，{}",e.getMessage());
        }

    }

    /**
     * 不传地区编码
     */
    @Test
    public void getEmptyCode() {
        try {
            Optional<Integer> op = this.temperatureServiceI.getTemperatureByLocalCode(null,"04","122324");
        }catch (Exception e){
            logger.error("获取该地区温度异常，{}",e.getMessage());
        }

    }


}
