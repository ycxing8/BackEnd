package com.seu.monitor.controller;

import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.ReportForm;
import com.seu.monitor.repository.ReportFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

@RestController
@RequestMapping(value = "/api/report_form")
public class ReportFormController {
    @Autowired
    private ReportFormRepository reportFormRepository;

    @PostMapping(value = "/get_by_date")
    public List<ReportForm> getByDate(@RequestParam("machine_identifier")String machineIdentifier,
                                      @RequestParam("date")String date,
                                      HttpSession session){
        //date 格式为：yyyy-MM-dd
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.NORMAL_USER)){
            return new ArrayList<>();
        }
        return reportFormRepository.findByMachineIdentifierAndDate(machineIdentifier,date);
    }

    @PostMapping(value = "/get_by_month")
    public List<List<ReportForm>> getByMonth(@RequestParam("machine_identifier")String machineIdentifier,
                                             @RequestParam("date")String date,
                                             HttpSession session){
        //date 格式为：yyyy-MM
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.NORMAL_USER)){
            return new ArrayList<>();
        }
        List<List<ReportForm>> reportFormList = new ArrayList<List<ReportForm>>();
        StringTokenizer token = new StringTokenizer(date,"-");
        String year = token.nextToken();
        String month= token.nextToken();
        int dayOfTheMonth = 0;
        try{
            int yearInt = Integer.parseInt(year);
            int monthInt = Integer.parseInt(month);
            dayOfTheMonth = getDaysByYearMonth(yearInt,monthInt);
        }catch (Exception e){
            e.printStackTrace();
        }
        for(int i = 0; i < dayOfTheMonth; i++) {
            String dayDate;
            if(i < 10){
                dayDate = "0" + i;
            }else {
                dayDate = i + "";
            }
            dayDate = date + "-" + dayDate;
            List<ReportForm> reportForms = reportFormRepository.findByMachineIdentifierAndDate(machineIdentifier,dayDate);
            if(reportForms.size() != 0) {
                reportFormList.add(reportForms);
            }
        }

        return reportFormList;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    public static int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
