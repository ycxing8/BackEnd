package com.seu.monitor.socket;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class SendFormChangeThread extends Thread {
    private Socket socket;
    private String machineIdentifier;
    public static List<String> messageListToMachine = new ArrayList<String>();

    public SendFormChangeThread(Socket socket, String machineIdentifier){
        this.socket = socket;
        this.machineIdentifier = machineIdentifier;
    }

    public void run(){
        while(messageListToMachine.size() > 0){
            String str = messageListToMachine.get(0);
            try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



    }


}
