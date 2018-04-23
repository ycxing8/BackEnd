package com.seu.monitor.controller;

import com.seu.monitor.TestSomeFun;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.config.ReportFormConfig;
import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.ReportForm;
import com.seu.monitor.repository.ReportFormRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

import static com.seu.monitor.config.ReportFormConfig.numOfMotor;
import static com.seu.monitor.config.ReportFormConfig.numOfProduceData;

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
        List<ReportForm> reportForms = reportFormRepository.findByMachineIdentifierAndDate(machineIdentifier,date);
        List<ReportForm> reportFormList = new ArrayList<ReportForm>();
        for(int i = 0; i < reportForms.size(); i++) {
            ReportForm reportForm = reportForms.get(i);
            reportForm.setId(i + 1);
            try {
                double temp = Double.parseDouble(reportForm.getData());
                reportForm.setData(String.format("%.4f", temp));
            }catch (Exception e){
                e.printStackTrace();
            }
                reportFormList.add(reportForm);
        }
        return reportFormList;
    }

    @PostMapping(value = "/get_by_month")
    public List<ReportForm> getByMonth(@RequestParam("machine_identifier")String machineIdentifier,
                                             @RequestParam("date")String date,
                                             HttpSession session) {
        return getMonthReport(machineIdentifier,date,session);
    }
    private List<ReportForm> getMonthReport(String machineIdentifier, String date,
                                       HttpSession session){
        //date 格式为：yyyy-MM
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.NORMAL_USER)){
            return new ArrayList<>();
        }
        final String[] reportFormSum = {
                "一段给料泵总","一段循环泵总","二段给料泵总","二段循环泵总","公用清洗泵总",
                "一段入料总","一段出料总","一段产水总",
                "二段入料总","二段出料总","二段产水总"
        };
        //Map sumMap = new HashMap();
        double sum[] = new double[numOfMotor + numOfProduceData];
        for(int i = 0;i<numOfMotor + numOfProduceData;i++){
            sum[i] = 0;
            //sumMap.put(ReportFormConfig.reportFormContent[i],sum[i]);
        }
        List<ReportForm> reportFormList = new ArrayList<ReportForm>();
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
        int index = 0;
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
                for(int s = 0; s < reportForms.size(); s++) {
                    index++;
                    ReportForm reportForm = reportForms.get(s);
                    reportForm.setId(index);
                    try{
                        double temp = Double.parseDouble(reportForm.getData());
                        reportForm.setData(String.format("%.4f", temp));
                        for(int f = 0; f < numOfMotor + numOfProduceData; f++){
                            if(reportForm.getContent().equals(ReportFormConfig.reportFormContent[f])) {
                                sum[f] += temp;
                                break;
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    reportFormList.add(reportForm);

                }
            }
        }
        for(int i = 0;i<numOfMotor + numOfProduceData;i++){
            ReportForm reportForm = new ReportForm();
            index++;
            reportForm.setId(index);
            reportForm.setDate(date);
            reportForm.setContent(reportFormSum[i]);
            reportForm.setMachineIdentifier(machineIdentifier);
            reportForm.setData(String.format("%.4f", sum[i]));
            if(i < numOfMotor){
                reportForm.setUnit(ReportFormConfig.timeUnit);
            }else{
                reportForm.setUnit(ReportFormConfig.volumeUnit);
            }
            reportFormList.add(reportForm);
        }
        return reportFormList;
    }

    @PostMapping(value = "/get_report_file_url")
    public String getReportFileUrl(@RequestParam("machine_identifier")String machineIdentifier,
                                   @RequestParam("date")String date,
                                   HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.NORMAL_USER)){
            return "没有权限！";
        }
        List<ReportForm> reportFormList = getMonthReport(machineIdentifier,date,session);
        Date nowDate = new Date();
        String str = DigestUtils.md5Hex(nowDate.getTime()+"");
        String cvsName = machineIdentifier + "设备" + date + "报表.csv";
        String path= "target/classes/static/report_form/" + str;//所创建文件的路径
        String url = "report_form/" + str + "/" + cvsName;
        generateCsv(path,cvsName,reportFormList);
        return url;
    }

    private void generateCsv(String path, String cvsName, List<ReportForm> reportFormList){

        try {
            File file = createFile(path,cvsName);
            FileOutputStream out = new FileOutputStream(file);
            byte [] bs = { (byte)0xEF, (byte)0xBB, (byte)0xBF};   //BOM
            out.write(bs);
            out.write("编号,日期,名称,数据,单位\r\n".getBytes("utf-8"));
            for(int i = 0; i < reportFormList.size(); i++){
                ReportForm reportForm = reportFormList.get(i);
                String str = reportForm.getId() + "," +
                        reportForm.getDate() + "," +
                        reportForm.getContent() + "," +
                        reportForm.getData() + "," +
                        reportForm.getUnit() + "\r\n";
                out.write(str.getBytes("utf-8"));
            }
            out.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File createFile(String path, String cvsName){

        File f = new File(path);

        if(!f.exists()){

            f.mkdirs();//创建目录
        }

        String fileName = cvsName;//文件名及类型

        File file = new File(path, fileName);

        if(!file.exists()){

            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
        return file;

    }

    /**
     * 根据年 月 获取对应的月份 天数
     */
    private int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

}
