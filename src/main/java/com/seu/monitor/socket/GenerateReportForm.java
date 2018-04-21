package com.seu.monitor.socket;

import com.seu.monitor.config.ReportFormConfig;
import com.seu.monitor.utils.ComponentUtils;
import com.seu.monitor.utils.ReportFormUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.seu.monitor.config.ReportFormConfig.numOfMotor;
import static com.seu.monitor.config.ReportFormConfig.numOfProduceData;

public class GenerateReportForm extends Thread {
    public static Map ReportFormExample = new HashMap();
    private String machineIdentifier;
    private long mLastStartTime[] = new long[numOfMotor];

    private double mTempRunTime[] = new double[numOfMotor];

    private boolean mStatus[] = new boolean[numOfMotor];

    private double lastProduceData[] = new double[numOfProduceData];


    public GenerateReportForm(String machineIdentifier) {
        this.machineIdentifier = machineIdentifier;
        ReportFormExample.put(machineIdentifier, this);
        initLastProduceData();
    }

    public void run() {
        //SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //dateFormat.format(new Date());

        int index = 0;
        while (true) {
            for(int i = 0; i < numOfMotor; i++) {
                recordMotorRunTime(i);
            }
            index++;
            if(index >= 10) {//100s执行一次
                for (int i = 0; i < ReportFormConfig.numOfProduceData; i++) {
                    recordProduceData(i);
                }
                index = 0;
            }

            try {
                sleep(10000);//10s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void recordProduceData(int i) {
        String data = ComponentUtils.getRealTimeData(machineIdentifier, ReportFormConfig.produceDateIdentifiers[i]);
        double tempData = 0;
        try{
            tempData = Double.parseDouble(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        double increaseData = tempData - lastProduceData[i];
        lastProduceData[i] = tempData;
        ReportFormUtils.addProduceData(machineIdentifier,increaseData,i);

    }

    private void initLastProduceData(){
        for(int i = 0; i < numOfProduceData; i++){
            String data = ComponentUtils.getRealTimeData(machineIdentifier, ReportFormConfig.produceDateIdentifiers[i]);
            double tempData = 0;
            try{
                tempData = Double.parseDouble(data);
            }catch (Exception e){
                e.printStackTrace();
            }
            lastProduceData[i] = tempData;
        }
    }

    private void recordMotorRunTime(int i){
        boolean tempStatus = ComponentUtils.getComponentStatus(machineIdentifier,ReportFormConfig.motorIdentifier[i]);

        //上升沿
        if(tempStatus && !mStatus[i]) {
            Date date = new Date();
            mLastStartTime[i] = date.getTime();
        }

        //下降沿
        if(!tempStatus && mStatus[i]) {
            Date date = new Date();
            mTempRunTime[i] = (double)(date.getTime() - mLastStartTime[i])/(1000.0*3600.0);
            ReportFormUtils.addMotorRunTime(machineIdentifier,mTempRunTime,i);
        }

        mStatus[i] = tempStatus;

    }

}
