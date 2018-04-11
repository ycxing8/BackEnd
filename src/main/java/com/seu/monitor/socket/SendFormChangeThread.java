package com.seu.monitor.socket;

import com.seu.monitor.config.ComponentConfig;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendFormChangeThread extends Thread {
    private Socket socket;
    private String machineIdentifier;
    public static List<String> messageListToMachine = new ArrayList<String>();
    private OutputStream outputStream = null;

    public SendFormChangeThread(Socket socket, String machineIdentifier){
        this.socket = socket;
        this.machineIdentifier = machineIdentifier;
        System.out.println("Send message form change thread start!");
        //System.out.println(machineIdentifier);
    }

    //public SendFormChangeThread(){}

    public void run(){
        if(outputStream == null){
            try {
                outputStream = socket.getOutputStream();
            }catch (Exception e){
                System.out.println("get output stream fail.");
                e.getStackTrace();
            }

        }


        //循环过程
        //1.扫描List,找到一个发与本设备的消息中，或者找不到，（此过程同步）
        //2.找到则格式转换然后发出去，找不到则下一步
        //3.等待100ms
        byte[] temp = new byte[3];

        while (true){
            /*for(int i = 0; i < messageListToMachine.size(); i++) {
                String message = messageListToMachine.get(i);
                System.out.println(message);
                messageListToMachine.remove(i);
                System.out.println(messageListToMachine.size());
            }*///for test

            //第一步&第2步
            synchronized(messageListToMachine){
                for(int i = 0; i < messageListToMachine.size(); i++){
                    String message = messageListToMachine.get(i);
                    System.out.println(message);

                    //01 TJF1 K 54 %
                    //设备号 部件号 状态 数据 单位
                    //0   1  2  3  4
                    //单位可省略

                    //temp说明
                    // 0      1       2
                    //部件 状态  数据
                    String [] arr = message.split("\\s+");
                    //System.out.println(arr[0]);
                    //System.out.println(arr[1]);
                    //System.out.println(arr[2]);
                    //System.out.println(arr[3]);
                    if(machineIdentifier.equals(arr[0])){
                        messageListToMachine.remove(i);
                        System.out.println(messageListToMachine.size());

                        temp[0] = (byte)(int)ComponentConfig.SEND_IDENTIFIER_MAP.get(arr[1]);
                        switch (arr[2]){
                            case "K":
                                temp[1] = 75;//K的ASCII
                                break;
                            case "G":
                                temp[1] = 71; //G的ASCII
                                break;
                            default:
                                temp[1] = 0;
                        }
                        try {
                            temp[2] = (byte)Integer.parseInt(arr[3]);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        System.out.println(temp[0]&0xff);
                        System.out.println(temp[1]&0xff);
                        System.out.println(temp[2]&0xff);

                        try{
                            outputStream.write(temp);
                            outputStream.flush();
                        }catch (Exception e){
                            e.getStackTrace();
                        }

                        //如果找到，则break
                       break;
                    }
                }
            }


            //第3步
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }



    }


}
