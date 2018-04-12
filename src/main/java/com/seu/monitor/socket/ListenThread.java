package com.seu.monitor.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ListenThread extends Thread{
    ServerSocket serverSocket;
    public ListenThread(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void run(){
        while(true){
            try {
                Socket socket = serverSocket.accept();
                SocketProcessThread socketProcessThread = new SocketProcessThread(socket);
                socketProcessThread.start();
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }catch (IOException e){
                System.out.println("socket accept fail！");
            }
        }
    }

}
