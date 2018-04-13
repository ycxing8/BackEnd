package com.seu.monitor.socket;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.config.SocketConfig;
import com.seu.monitor.socket.SocketProcessThread;
import com.seu.monitor.utils.MachineUtils;

import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendFormChangeThread extends Thread {
    private Socket socket;
    private String machineIdentifier;
    public static List<String> messageListToMachine = new ArrayList<String>();
    private OutputStream outputStream = null;

    public static Map sendFormChangeThreadMap = new HashMap();

    public SendFormChangeThread(Socket socket, String machineIdentifier){
        this.socket = socket;
        this.machineIdentifier = machineIdentifier;
        System.out.println("Send message form change thread start!");
        //System.out.println(machineIdentifier);
    }

    //public SendFormChangeThread(){}
    public void run(){
        synchronized (sendFormChangeThreadMap) {
            sendFormChangeThreadMap.put(this.toString(), machineIdentifier);
        }
        try{
            sendFormChange();
        }catch (Exception e){
            e.getStackTrace();
        }finally {
            System.out.println("send form change thread stop.");
            MachineUtils.changeStatus(machineIdentifier,MachineConfig.disConnect);
            synchronized (sendFormChangeThreadMap) {
                sendFormChangeThreadMap.remove(this.toString());
            }
        }
    }
    public void sendFormChange(){
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
        byte[] temp = new byte[4];

        while (SocketProcessThread.judgeOnline(machineIdentifier)){
            /*for(int i = 0; i < messageListToMachine.size(); i++) {
                String message = messageListToMachine.get(i);
                System.out.println(message);
                messageListToMachine.remove(i);
                System.out.println(messageListToMachine.size());
            }*///for test

            boolean status = false;

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
					try{
						String [] arr = message.split("\\s+");
						int index = 0;
						for(String ss :arr){

							switch (index){
								case 0:
									if(machineIdentifier.equals(arr[0])) {
										messageListToMachine.remove(i);
										status = true;
									}
									break;
								case 1:
									temp[0] = (byte)(int)ComponentConfig.SEND_IDENTIFIER_MAP.get(arr[1]);
									break;
								case 2:
									if(temp[0] == 1){
										temp[1] = (byte)(int)MachineConfig.SEND_MACHINE_STATUS_MAP.get(arr[2]);

									}else {
										switch (arr[2]) {
											case "K":
												temp[1] = 75;//K的ASCII
												break;
											case "G":
												temp[1] = 71; //G的ASCII
												break;
											default:
												temp[1] = 0;
										}
									}
									break;
								case 3:
									try {
										int anInt = Integer.parseInt(arr[3]);
										if(temp[0] >= ComponentConfig.needChange[0]&&
												temp[0]<= ComponentConfig.needChange[1]){
											anInt = openingTransform(anInt);
										}
										temp[3] = (byte)anInt;
										temp[2] = (byte)(anInt/256);
									} catch (NumberFormatException e) {
										e.printStackTrace();
									}
									break;
							}
							index++;
						}

                        if(status == true) {
                            System.out.println(temp[0]&0xff);
                            System.out.println(temp[1]&0xff);
                            System.out.println(temp[2]&0xff);
                            System.out.println(temp[3]&0xff);

                            try{
                                outputStream.write(temp);
                                outputStream.flush();
                            }catch (Exception e){
                                e.getStackTrace();
                            }
                            //如果找到，则break
                            break;
                        }
					}catch(Exception e){
						e.printStackTrace();
					}


                }
            }


            //第3步
            //如果没有命令，则发送心跳包
            if(status == false) {
                try{
                    outputStream.write(SocketConfig.HEARTBEAT);
                    outputStream.flush();
                }catch (Exception e){
                    e.getStackTrace();
                }
            }
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private int openingTransform(int opening){
        return (int)(opening*221.18+5530);
    }

}
