package com.dtc.java.shucang.JFSBWGBGJ.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Author : lihao
 * Created on : 2020-03-24
 * @Description : TODO描述类作用
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZongShu {
    private String room;
    private String partitions;
    private String box;
    private double num;
    private int flag;
}
