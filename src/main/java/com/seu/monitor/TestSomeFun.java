package com.seu.monitor;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.socket.ReceiveFormChangeThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.*;

import com.seu.monitor.socket.SendFormChangeThread;
import com.seu.monitor.socket.SocketProcessThread;
import org.apache.commons.codec.digest.DigestUtils;

public class TestSomeFun {
    public static void main(String[] args) {
        test8();
    }

    public static void test8(){
        String i = "1";
        try {
            float f = (float) -0.5;
            System.out.println(f);
            System.out.println((int)f);
            System.out.println(i);
            TestSomeFun1 testx = new TestSomeFun1(i);
            testx.changeStatus();
            System.out.println(i);

           System.out.println("run here.");
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            System.out.println("Receive message form change thread end!");
        }
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
