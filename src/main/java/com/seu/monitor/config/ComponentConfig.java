package com.seu.monitor.config;

import com.seu.monitor.entity.Component;

import java.util.ArrayList;
import java.util.List;

public class ComponentConfig {
    /*public static final List<String> componentIdentifierList =
            new ArrayList<String>(){{
                add("F1");add("F2");add("F1");
    }};*/
    public static final String[] componentIdentifiers =
            {"MACHINE",
            "F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13",
            "P1","P2","P3","P4","P5","P6",
            "L1","L2","L3","L4",
            "T1","T2","T3",
            "KGF1","KGF2","KGF3","KGF4","KGF5","KGF6","KGF7","KGF8","KGF9",
           "TJF1","TJF2","TJF3","TJF4","TJF5","TJF6","TJF7","TJF8",
            "M1","M2","M3","M4","M5",
            "SIF1","SIF2",   //进料总量
            "SWF1","SWF2",  //产水总量
            "SOF2",         //出料总量
            "PIF1","PIF2",//单次进料
            "PWF1","PWF2"//单次产水
            };

    //各单位数据个数与前面数据之和
    public static final Integer[] componentNum = {
    1, //N
    1+13, //M3/H
    1+13+6,//MPA
    1+13+6+4,//CM
    1+13+6+4+3,//C
    1+13+6+4+3+9,//N
    1+13+6+4+3+9+8,// %
    1+13+6+4+3+9+8+5,//HZ
    1+13+6+4+3+9+8+5+9//M3
     };

    public static final String[] componentUnits = {
            "N","M3/H","MPA","CM","C","N","%","HZ","M3"

    };
    public static final String defaultUnit ="N";

}
