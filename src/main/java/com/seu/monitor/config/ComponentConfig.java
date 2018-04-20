package com.seu.monitor.config;

import com.seu.monitor.entity.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComponentConfig {
    /*public static final List<String> componentIdentifierList =
            new ArrayList<String>(){{
                add("F1");add("F2");add("F1");
    }};*/
    public static final String componentStatusOpen ="K";
    public static final String componentStatusClose ="G";
    public static final String componentStatusNone ="N";
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

    public static final Integer noneStatusAfterNum = 1+13+6+4+3+9+8+5;
    public static final Integer haveStatusAfterNum = 1+13+6+4+3;
    //设备状态数据所在序号
    public static final Integer machineStatusNum = 1;

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
    public static final String[] componentKinds = {
            "设备","流量计","压力计","液位计","温度计","开关阀","调节阀","泵","生产数据"
    };
    public static final String defaultKind ="设备组件";
    public static final String defaultUnit ="N";

    public static final String[] componentNames = {"MACHINE",
            "F1","F2","F3","F4","F5","F6","F7","F8","F9","F10","F11","F12","F13",
            "一段进膜压力","一段浓侧压力","一段清侧压力","二段进膜压力","二段浓侧压力","二段清侧压力",
            "一段给料箱液位","公用清洗水箱液位","二段给料箱液位","浓缩液贮罐液位",
            "一段进膜温度","二段进膜温度","公用清洗水箱水温",
            "KGF1","KGF2","KGF3","KGF4","KGF5","KGF6","KGF7","KGF8","KGF9",
            "TJF1","TJF2","TJF3","TJF4","TJF5","TJF6","TJF7","TJF8",
            "一段给料泵","一段循环泵","二段给料泵","二段循环泵","公用清洗泵",
            "一段进料总量","二段进料总量",   //进料总量
            "一段产水总量","二段产水总量",  //产水总量
            "二段出料总量",         //出料总量
            "一段单次进料","二段单次进料",//单次进料
            "一段单次产水","二段单次产水"//单次产水
    };

    public static final String[] sendIdentifiers = {
            "MACHINE","KGF1","KGF2","KGF3","KGF4","KGF5","KGF6","KGF7","KGF8","KGF9",
            "TJF1","TJF2","TJF3","TJF4","TJF5","TJF6","TJF7","TJF8",
            "M1","M2","M3","M4","M5"
    };
    public static final int[] needChange ={11,18};//调节阀开度做27648-5630（0-100）的转换
    public static final Map SEND_IDENTIFIER_MAP = new HashMap(){{
        put("MACHINE",1);
        put("KGF1",2);
        put("KGF2",3);
        put("KGF3",4);
        put("KGF4",5);
        put("KGF5",6);
        put("KGF6",7);
        put("KGF7",8);
        put("KGF8",9);
        put("KGF9",10);
        put("TJF1",11);
        put("TJF2",12);
        put("TJF3",13);
        put("TJF4",14);
        put("TJF5",15);
        put("TJF6",16);
        put("TJF7",17);
        put("TJF8",18);
        put("M1",19);
        put("M2",20);
        put("M3",21);
        put("M4",22);
        put("M5",23);
    }};

}
