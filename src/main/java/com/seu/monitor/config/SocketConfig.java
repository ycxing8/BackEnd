package com.seu.monitor.config;

import java.util.ArrayList;
import java.util.List;

//这个类存放socket server 与PLC 通信所使用的静态变量和参数
public class SocketConfig {
    final public static int PORT = 8082;

    //final public static int START_FLAG = 41445;
    //final public static int END_FLAG = 58785;

    final public static byte[] FRAME_START_FLAG_FROM_FLOAT =
            {(byte)0xff,(byte)0xff,(byte)0xff,(byte)0xff};

}
