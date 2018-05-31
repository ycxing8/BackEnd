package com.seu.monitor;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.socket.ReceiveFormChangeThread;
import java.util.Random;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.*;

import com.seu.monitor.socket.SendFormChangeThread;
import com.seu.monitor.socket.SocketProcessThread;
import com.seu.monitor.utils.ComponentUtils;
import com.seu.monitor.utils.MachineUtils;
import com.seu.monitor.utils.ReportFormUtils;
import org.apache.commons.codec.digest.DigestUtils;

public class TestSomeFun {
    public static void main(String[] args) {
        test13();
    }

    public static void test13(){
        double []data = new double[11];
        for(int i = 1; i <= 6; i++){
            String date = "2018-06-0";
            date = date + (i + "");
            double run1time = Math.random()*2 + 4;
            double run2time = Math.random()*2 + 3;
            double run3time = Math.random() + 0.5;
            double out1 = Math.random()*0.5 + 2.5;
            double out2 = Math.random()*0.2 + 0.4;
            data[0] = 1.32 * run1time * out1;
            data[1] = 0.32 * run1time * out1;
            data[2] = run1time * out1;
            data[3] = data[1];
            data[4] = data[3] - run2time * out2;
            data[5] = run2time * out2;
            data[6] = run1time;
            data[7] = data[6];
            data[8] = run2time;
            data[9] = data[8];
            data[10] = run3time;

            ReportFormUtils.addDayReportForm("01", date, data);
        }


    }
    private static void test12(){
        String sb = "1234567";
        String x = sb.substring(0, 5);
        System.out.println(x);
    }
    private static void test11(){
        Set names=Charset.availableCharsets().keySet();
        for (Iterator iter = names.iterator(); iter.hasNext();) {
            String charsetName = (String) iter.next();
            if (Charset.isSupported(charsetName)) {
                System.out.println(charsetName);
            }
        }
    }
    public static void createFile(){

        String path= "测试";//所创建文件的路径

        File f = new File(path);

        if(!f.exists()){

            f.mkdirs();//创建目录
        }

        String fileName = "abc.txt";//文件名及类型

        File file = new File(path, fileName);

        if(!file.exists()){

            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    }
    public static void test10(){
        java.util.Calendar date = java.util.Calendar.getInstance();
        System.out.println(date);
        System.out.println(date.getActualMaximum(date.DAY_OF_MONTH));
    }
    public static void test9(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while (true){
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //boolean b = MachineUtils.ifOnline("01");
            boolean b = ComponentUtils.getComponentStatus("01","M1");
            System.out.println(b);
        }


    }

    public static void test8(){
        Test test = new Test();
        System.out.println(test);
        System.out.println(test.getStatus());
        TestSomeFun1 test1 = new TestSomeFun1(test);
        test1.changeStatus();
        System.out.println(test.getStatus());
    }
    public static void test7(){
        Map map = new HashMap();
        map.put("01",true);
        map.put("02",true);
        map.put("03",false);
        System.out.println(map);
        map.remove("02");
        System.out.println(map);
        boolean b =(boolean)map.get("01");
        System.out.println(b);
    }
    public static void test6(){
        String testString = "01 TJF1 K ";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int num = 0;
        while (true) {
            String str = null;
            try {
                str = br.readLine();
                num += 1;
                String message = testString + num + " %";
                System.out.println(num);
                SendFormChangeThread.messageListToMachine.add(message);
                System.out.println(SocketProcessThread.judgeOnline("01"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void test5(){
        int x = (int)ComponentConfig.SEND_IDENTIFIER_MAP.get("M1");
        byte b = (byte)x;
        System.out.println(b);
        System.out.println(x);

    }
    private static void test4(){
        String str = DigestUtils.md5Hex(MachineConfig.firstMachineIdentifier);
        System.out.println(str);
    }

    private static void test3(){
        ReceiveFormChangeThread formChangeThread = new ReceiveFormChangeThread(new Socket(),"01");
        for(int i = 0; i <= ComponentConfig.componentIdentifiers.length; i++){
            System.out.println(i+": "+ formChangeThread.getUnit(i));
        }
    }
    private static void test2(){
        List<String> stringList = new ArrayList<String>();
        System.out.println(stringList.size());
        stringList.add("a");
        stringList.add("b");
        stringList.add("c");
        System.out.println(stringList.size());
        System.out.println(stringList.get(0));
        System.out.println(stringList.get(1));
        System.out.println(stringList.get(2));
        stringList.remove(0);
        System.out.println(stringList.size());
        System.out.println(stringList.get(0));
        System.out.println(stringList.get(1));
    }

    private static void test1(){
        byte[] x = {1,2,3,4};
        byte[] y = {1,2,3,4};
        ReceiveFormChangeThread formChangeThread = new ReceiveFormChangeThread(new Socket(),"01");
        System.out.println(formChangeThread.bytesEquals(x, y));
    }

    private static void test(){
        //测试用
        byte[] b = {0x41,(byte)0xc8,0,0};
        b[0] = (byte)65;
        b[1] = (byte)65;
        b[2] = (byte)194;
        b[3] = (byte)143;
        ReceiveFormChangeThread formChangeThread = new ReceiveFormChangeThread(new Socket(),"01");
        System.out.println(formChangeThread.byte4ToFloat(b));
    }

}
