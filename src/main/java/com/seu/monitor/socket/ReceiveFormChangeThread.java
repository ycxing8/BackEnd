package com.seu.monitor.socket;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.config.SocketConfig;
import com.seu.monitor.utils.MachineUtils;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.seu.monitor.config.ComponentConfig.componentIdentifiers;
import static com.seu.monitor.config.ComponentConfig.componentUnits;


public class ReceiveFormChangeThread extends Thread {
    private InputStream inputStream;
    private Socket socket;
    private String machineIdentifier;
    public List<String> messageListFromMachine = new ArrayList<String>();
    public static Map receiveFormChangeThreadMap = new HashMap();

    public ReceiveFormChangeThread(Socket socket, String machineIdentifier) {
        this.socket = socket;
        this.machineIdentifier = machineIdentifier;
        System.out.println("Receive message form change thread start!");
    }
    public void run(){
        try {
            synchronized (receiveFormChangeThreadMap){
                receiveFormChangeThreadMap.put(this.toString(),machineIdentifier);
            }
            ReceiveFormChangeFromFloat();
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            System.out.println("Receive message form change thread stop.");
            MachineUtils.changeStatus(machineIdentifier,MachineConfig.disConnect);
            synchronized (receiveFormChangeThreadMap){
                receiveFormChangeThreadMap.remove(this.toString());
            }
        }
    }


    public float byte4ToFloat(byte[] b) {
        //byte[] b = {0,(byte)0,0,0};
        try{
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(b));
            float f = dis.readFloat();
            dis.close();
            //System.out.println(f);
            return f;
        } catch (IOException e){
            System.out.println("byte4 to float fail!");
            return 0;
        }
    }

    public boolean bytesEquals(byte[] a, byte[] b){
        if(a.length != b.length){
            return false;
        }
        for(int i = 0; i < a.length; i++){
            if(a[i] != b[i]){
                return false;
            }
        }
        return true;
    }

    public String getUnit(Integer index){
        for(int i = 0; i < ComponentConfig.componentNum.length; i++){
            if(index <= ComponentConfig.componentNum[i]) {
                return ComponentConfig.componentUnits[i];
            }
        }

        return ComponentConfig.defaultUnit;
    }

    private void ReceiveFormChangeFromFloat(){
        try{
            /*if(messageListFromMachine == null){
                messageListFromMachine = new ArrayList<String>();
            }*/
            if(inputStream == null){
                inputStream = socket.getInputStream();
            }

            Integer index = 0;
            byte[] temp = {0,0,0,0};
            Boolean startStatus = false;
            float data = 0;

            //进入接收的数据格式转换
            while (SocketProcessThread.judgeOnline(machineIdentifier)){

                //每个循环中，
                //读数据->判断起始标志->按序号解析数据->判断是否帧结束

                //读取一个float数的四个字节
                for(int i = 0;i < 4; i++){
                    int x = inputStream.read();
                    temp[i] = (byte)x;
                    while(x == -1) {
                        sleep(10);//如果数据连续，则没有等待
                        x = inputStream.read();
                        temp[i] = (byte)x;
                    }
                }

                System.out.println(temp[0]&0xff);
                System.out.println(temp[1]&0xff);
                System.out.println(temp[2]&0xff);
                System.out.println(temp[3]&0xff);

                //判断是否是帧开始的Flag
                if(startStatus == false && bytesEquals(temp, SocketConfig.FRAME_START_FLAG_FROM_FLOAT)){
                    startStatus = true;
                    System.out.println("A frame start.");
                    continue;
                }


                //进行数据转化
                if(startStatus == true){
                    index++;
                    //如果是设备状态数据
                    if(index == ComponentConfig.machineStatusNum){
                        data = byte4ToFloat(temp);
                        String machineStatus = getMachineStatusByNum(data);
                        MachineUtils.changeStatus(machineIdentifier,machineStatus);
                        synchronized (messageListFromMachine) {
                            messageListFromMachine.add(componentIdentifiers[index - 1] + " "
                            + machineStatus + " N  N");
                        }
                        continue;

                    }
                    String str = componentIdentifiers[index - 1];
                    //System.out.println(str);
                    str += " N ";
                    data = byte4ToFloat(temp);
                    //System.out.println("data:" + data);
                    str += (data + " ");//把数字转化为字符串
                    str += getUnit(index);
                    synchronized (messageListFromMachine) {
                        messageListFromMachine.add(str);
                    }
                    System.out.println(str);
                    //System.out.println(messageListFromMachine.size()+
                      //      ":"+messageListFromMachine.get(0));
                }

                //一帧结束
                if(index >= componentIdentifiers.length){
                    index = 0;
                    startStatus = false;
                    continue;
                }

            }

        }catch (IOException e){
            System.out.println("Can't get input stream form socket!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private String getMachineStatusByNum(float num){
        int index = ((int)(num + 1.5));
        if(index >= MachineConfig.ReceiveMachineStatus.length
                || index < 0){
            return "NULL";
        }
        return MachineConfig.ReceiveMachineStatus[index];
    }


/*
    public void run(){
        try{
            if(messageListFromMachine == null){
                messageListFromMachine = new ArrayList<String>();
            }
            if(inputStream == null){
                inputStream = socket.getInputStream();
            }
            int index = 0;
            Boolean startStatus = false;
            while (true){
                int tempH = inputStream.read();
                while(tempH == -1){
                    sleep(1);
                    tempH = inputStream.read();
                }
                System.out.println(tempH);
                int tempL = inputStream.read();
                while(tempL == -1){
                    tempL = inputStream.read();
                    sleep(1);
                }
                System.out.println(tempL);
                int temp = tempH * 256 + tempL;
                System.out.println(temp);
                if(temp == SocketConfig.START_FLAG){
                    startStatus = true;
                    System.out.println("start!");
                    continue;
                }
                if(startStatus == true){
                    String str = componentIdentifiers[index];
                    System.out.println(str);
                    str += " N ";
                    str += (temp + " ");//把数字转化为字符串
                    str += componentUnits[0];
                    messageListFromMachine.add(str);
                    System.out.println(str);
                    System.out.println(messageListFromMachine.size()+
                    ":"+messageListFromMachine.get(0));
                    index++;
                }
                if(index >= componentIdentifiers.length){
                    index = 0;
                    startStatus = false;
                    continue;
                }

            }

        }catch (IOException e){
            System.out.println("Can't get input stream form socket!");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }*/

}
