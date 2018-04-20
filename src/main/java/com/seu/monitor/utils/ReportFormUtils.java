package com.seu.monitor.utils;

import com.seu.monitor.config.ReportFormConfig;
import com.seu.monitor.entity.ReportForm;
import com.seu.monitor.repository.ReportFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
public class ReportFormUtils {
    @Autowired
    private ReportFormRepository reportFormRepository;
    public static ReportFormUtils reportFormUtils;
    @PostConstruct
    public void init() {
        reportFormUtils = this;
        reportFormUtils.reportFormRepository = this.reportFormRepository;
    }

    public static void addMotorRunTime(String machineIdentifier, double[] tempRunTime, int i){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        ReportForm reportForm = null;
        List<ReportForm> reportForms = ReportFormUtils.reportFormUtils.reportFormRepository.
                findByMachineIdentifierAndDateAndContent(machineIdentifier,date,ReportFormConfig.reportFormContent[i]);
        if(reportForms.size() == 0){
            reportForm = new ReportForm();
            reportForm.setDate(date);
            reportForm.setMachineIdentifier(machineIdentifier);
            reportForm.setContent(ReportFormConfig.reportFormContent[i]);
            reportForm.setUnit(ReportFormConfig.timeUnit);
        }else {
            reportForm = reportForms.get(0);
        }
        double temp = 0;
        try {
            temp = Double.parseDouble(reportForm.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
        temp += tempRunTime[i];
        reportForm.setData(temp + "");
        ReportFormUtils.reportFormUtils.reportFormRepository.save(reportForm);
    }

    public static void addProduceData(String machineIdentifier, double increaseData, int i){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());
        ReportForm reportForm = null;
        List<ReportForm> reportForms = reportFormUtils.reportFormRepository.
                findByMachineIdentifierAndDateAndContent(machineIdentifier,date,ReportFormConfig.reportFormContent[ReportFormConfig.numOfMotor + i]);
        if(reportForms.size() == 0){
            reportForm = new ReportForm();
            reportForm.setDate(date);
            reportForm.setMachineIdentifier(machineIdentifier);
            reportForm.setContent(ReportFormConfig.reportFormContent[ReportFormConfig.numOfMotor + i]);
            reportForm.setUnit(ReportFormConfig.volumeUnit);
        }else {
            reportForm = reportForms.get(0);
        }
        double temp = 0;
        try {
            temp = Double.parseDouble(reportForm.getData());
        }catch (Exception e){
            e.printStackTrace();
        }
        temp += increaseData;
        reportForm.setData(temp + "");
        ReportFormUtils.reportFormUtils.reportFormRepository.save(reportForm);
    }

}
