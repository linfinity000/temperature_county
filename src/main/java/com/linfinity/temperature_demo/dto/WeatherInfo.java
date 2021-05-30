package com.linfinity.temperature_demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class WeatherInfo implements Serializable {

    private String AP;

    private String Radar;

    private String SD;

    private String WD;

    private String WS;

    private String WSE;

    private String city;

    private Integer cityid;

    private String isRadar;

    private String njd;

    private String sm;

    private String temp;

    private String time;
}
