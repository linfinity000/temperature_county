package com.linfinity.temperature_demo.controller;

import com.linfinity.temperature_demo.service.TemperatureServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/local/temperature/")
public class TemperatureController {


    @Autowired
    private TemperatureServiceI temperatureServiceI;



    /**
     * 根据省市区编码获取当天该地区温度
     * @param province      required = true
     * @param city          required = true
     * @param county        required = true
     * @return
     */
    @GetMapping("get/{province}/{city}/{county}")
    public Optional<Integer> getTemperature(
            @PathVariable(name = "province") String province,
            @PathVariable(name = "city") String city,
            @PathVariable(name = "county") String county) {
            return this.temperatureServiceI.getTemperatureByLocalCode(province,city,county);
    }
}
