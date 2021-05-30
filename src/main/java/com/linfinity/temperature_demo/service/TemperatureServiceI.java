package com.linfinity.temperature_demo.service;


import java.util.Optional;

public interface TemperatureServiceI {


    /**
     * 获取指定地区的温度
     * @param province  省编码
     * @param city      市编码
     * @param county    地区编码
     * @return
     */
    Optional<Integer> getTemperatureByLocalCode(String province,
                                                String city,
                                                String county);
}
