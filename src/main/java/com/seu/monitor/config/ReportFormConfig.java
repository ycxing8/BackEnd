package com.seu.monitor.config;

public class ReportFormConfig {

    //泵的命名必须在前面，程序通过顺序进行索引
    public static final String[] reportFormContent = {
            "一段给料泵","一段循环泵","二段给料泵","二段循环泵","公用清洗泵",
            "一段入料","一段出料","一段产水",
            "二段入料","二段出料","二段产水"
    };
    public static final String produceDateIdentifiers[] =
            {"SIF1", "SIF2", "SWF1", //一段出料等于二段进料
                    "SIF2","SOF2","SWF1"};


    public static final String volumeUnit = "立方米";
    public static final String timeUnit = "小时";
    public static final String[] motorIdentifier ={"M1","M2","M3","M4","M5"};
    public static final int numOfMotor = 5;
    public static final int numOfProduceData = 6;
}
