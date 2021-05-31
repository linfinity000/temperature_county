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
public class SimilaryInfo implements Serializable {

    /**
     * 相似度得分
     */
    private double score;

    /**
     * 地区编码（省、市、区）
     */
    private String code;
}
