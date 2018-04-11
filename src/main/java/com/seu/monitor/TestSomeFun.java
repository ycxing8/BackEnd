package com.seu.monitor;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.socket.ReceiveFormChangeThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.seu.monitor.socket.SendFormChangeThread;
import org.apache.commons.codec.digest.DigestUtils;

public class TestSomeFun {
    public static void main(String[] args) {
        test6();
    }

    public static void test6(){
        String testString = "01 TJF1 K 54 %";
        SendFormChangeThread.messageListToMachine.add(testString);
        SendFormChangeThread.messageListToMachine.add(testString);
        System.out.println(SendFormChangeThread.messageListToMachine.size());
        SendFormChangeThread.messageListToMachine.remove(0);
        System.out.println(SendFormChangeThread.messageListToMachine.size());
        //SendFormChangeThread sendChangeThread = new SendFormChangeThread(null,"01");
        //sendChangeThread.run();

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
        ReceiveFormChangeThread formChangeThread = new ReceiveFormChangeThread();
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
        ReceiveFormChangeThread formChangeThread = new ReceiveFormChangeThread();
        System.out.println(formChangeThread.bytesEquals(x, y));
    }

    private static void test(){
        //测试用
        byte[] b = {0x41,(byte)0xc8,0,0};
        b[0] = (byte)65;
        b[1] = (byte)65;
        b[2] = (byte)194;
        b[3] = (byte)143;
        ReceiveFormChangeThread formChangeThread = new ReceiveFormChangeThread();
        System.out.println(formChangeThread.byte4ToFloat(b));
    }

}
