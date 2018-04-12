package com.seu.monitor.socket;

import java.io.*;
import java.net.Socket;
import java.lang.Thread;

import com.seu.monitor.entity.ComponentLog;
import com.seu.monitor.entity.Machine;
import com.seu.monitor.utils.MachineUtils;

import java.util.*;
import java.text.SimpleDateFormat;

import static com.seu.monitor.utils.ComponentLogUtils.addComponentLog;
import static com.seu.monitor.utils.ComponentUtils.modifyComponentRealTimeData;

public class SocketProcessThread extends Thread{

    private Socket socket;
    private String machineIdentifier;

    public static Map socketProcessThreadMap = new HashMap();

    private static Map onlineMachineMap = new HashMap();
    //private static List<String> onlineMachineIdentifiers = new ArrayList<String>();

    public static boolean judgeOnline(String machineIdentifier){
        synchronized (onlineMachineMap){
            return (boolean)onlineMachineMap.get(machineIdentifier);
        }
    }
    private void otherSameMachineSignOut(){
        if(!onlineMachineMap.containsKey(machineIdentifier)){
            synchronized (onlineMachineMap) {
                onlineMachineMap.put(machineIdentifier, true);
            }
        }else{
            synchronized (onlineMachineMap) {
                onlineMachineMap.put(machineIdentifier, false);
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (onlineMachineMap) {
                onlineMachineMap.put(machineIdentifier, true);
            }
        }
    }

    public SocketProcessThread(Socket socket) {
        this.socket = socket;
        System.out.println("Connection accept socket:" + socket);
    }

    //由于PLC限制，PLC与Server使用简单的协议，使用格式转换线程进行转换
    public void run() {

        BufferedReader br = null;
        PrintWriter pw = null;
        //boolean status = false;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            pw.println("you connect!");
            pw.flush();

            String identityCode = br.readLine();//readLine会堵塞，直到遇到‘\n’
            System.out.println("Machine: " + identityCode + " connect");

            //进行身份验证
            Machine machine = MachineUtils.machineUtils.findByMachineIdentityCode(identityCode);
            //身份验证正确
            if(machine != null){
                machineIdentifier = machine.getIdentifier();
                System.out.println("Machine identity correct;" +
                        "ID: " + machine.getId() +
                        "; Name: " + machine.getName());
                        //+ "; " + machine.getIdentityCode());
                pw.println("Correct identity!");
                pw.flush();

                //判断是否该设备已在线
              /*  synchronized(onlineMachineIdentifiers) {
                    for (int i = 0; i < onlineMachineIdentifiers.size(); i++){
                        if(onlineMachineIdentifiers.get(i).equals(machineIdentifier)){
                            //如果设备已在线，则return，程序将执行final段
                            System.out.println("machine already exist.");
                            pw.println("machine already exist.");
                            pw.flush();
                            status = false;
                            return;

                        }
                    }
                    //该设备没有已经在线
                    onlineMachineIdentifiers.add(machineIdentifier);
                    status = true;
                }*/
                otherSameMachineSignOut();
                synchronized (socketProcessThreadMap) {
                    socketProcessThreadMap.put(this.toString(), machineIdentifier);
                }
                //打开数据收发格式转换线程
                ReceiveFormChangeThread receiveFormChangeThread = new ReceiveFormChangeThread(socket, machineIdentifier);
                receiveFormChangeThread.start();

                SendFormChangeThread sendFormChangeThread = new SendFormChangeThread(socket,machineIdentifier);
                sendFormChangeThread.start();


                //将接收数据存入数据库
                while (judgeOnline(machineIdentifier)){

                    //如果消息队列有数据，存入数据库
                    if(receiveFormChangeThread.messageListFromMachine != null &&
                            receiveFormChangeThread.messageListFromMachine.size() != 0){
                        String str = receiveFormChangeThread.messageListFromMachine.get(0);
                        receiveFormChangeThread.messageListFromMachine.remove(0);

                        //System.out.println("One log: " + str);

                        //such as : M1 K 30 Hz
                        //          F1 N 120 m3/h

                        String [] arr = str.split("\\s+");
                        ComponentLog componentLog = new ComponentLog();
                        componentLog.setMachineIdentifier(machineIdentifier);
                        int index = 0;
                        for(String ss : arr){
                            //System.out.println(ss);
                            index++;
                            switch (index){

                                case 1:
                                    componentLog.setComponentIdentifier(ss);
                                    break;
                                case 2:
                                    componentLog.setStatus(ss);
                                    break;
                                case 3:
                                    componentLog.setData(ss);
                                    break;
                                case 4:
                                    componentLog.setUnit(ss);
                                    break;
                                default:
                                    System.out.println("Error data form!");
                                    break;
                            }
                        }
                        if(index != 4){
                            System.out.println("Error data form!");
                        }else{
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            componentLog.setTime(dateFormat.format(new Date()));
                            addComponentLog(componentLog);
                            if(modifyComponentRealTimeData(componentLog)){
                                System.out.println(str + " add success!");
                            }else{
                                System.out.println("Not have this component!");
                            }
                        }

                    }else{
                        //避免扫描太快，占用多CPU计算时间
                        //如果消息队列没有数据，则等待
                        //有，则连续扫描
                        sleep(10);
                    }


                }



            } else {
                pw.println("Error identity");
                pw.flush();
            }

        }catch (Exception e){
            System.out.println("One socket use to connect machine disconnect");
            e.getStackTrace();
        }finally {
            synchronized(socketProcessThreadMap) {
                socketProcessThreadMap.remove(this.toString());
            }
            try{
                br.close();
                pw.close();
                socket.close();
                System.out.println("close this socket.");
            }catch (Exception e){
                System.out.println("One socket can't be close");
                e.getStackTrace();
            }

        }

    }

}
/*   //以读行的方式进行字符串操作
    public void run() {

        BufferedReader br = null;
        PrintWriter pw = null;
        String machineIdentifier;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            pw.println("you connected");
            pw.flush();

            InputStream inputStream = socket.getInputStream();
            int x = inputStream.read();
            System.out.println(x);
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(66);
            //outputStream.flush();

            String identityCode = br.readLine();//readLine会堵塞，直到遇到‘\n’
            System.out.println("Machine: " + identityCode + " connect");

            Machine machine = MachineUtils.machineUtils.findByMachineIdentityCode(identityCode);
            if(machine != null){
                machineIdentifier = machine.getIdentifier();
                System.out.println("A machine identity correct;" +
                        "ID: " + machine.getId() +
                        "; Name: " + machine.getName() +
                        "; IdentityCode: " + machine.getIdentityCode());
                pw.println("Correct identity");
                pw.flush();

                while (true){
                    String str = br.readLine();
                    pw.println("Server get a log!");
                    pw.flush();

                    String [] arr = str.split("\\s+");
                    System.out.println("One log from PLC is: " + str);
                    //such as : M1 K 30 Hz
                    //          F1 N 120 m3/h
                    ComponentLog componentLog = new ComponentLog();
                    componentLog.setMachineIdentifier(machineIdentifier);
                    int index = 0;
                    for(String ss : arr){
                        //System.out.println(ss);
                        index++;
                        switch (index){

                            case 1:
                                componentLog.setComponentIdentifier(ss);
                                break;
                            case 2:
                                componentLog.setStatus(ss);
                                break;
                            case 3:
                                componentLog.setData(ss);
                                break;
                            case 4:
                                componentLog.setUnit(ss);
                                break;
                            default:
                                System.out.println("Error data form!");
                                break;
                        }
                    }
                    if(index != 4){
                        System.out.println("Error data form!");
                    }else{
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        componentLog.setTime(dateFormat.format(new Date()));
                        addComponentLog(componentLog);
                        if(modifyComponentRealTimeData(componentLog)){
                            System.out.println(str + " add success!");
                        }else{
                            System.out.println("Not have this component!");
                        }
                    }

                }


            } else {
                pw.println("Error identity");
                pw.flush();
            }

        }catch (Exception e){
            System.out.println("One socket use to connect machine disconnect");
            e.getStackTrace();
        }finally {
            try{
                br.close();
                pw.close();
                socket.close();
            }catch (Exception e){
                System.out.println("One socket can't be close");
                e.getStackTrace();
            }

        }

    }*/
