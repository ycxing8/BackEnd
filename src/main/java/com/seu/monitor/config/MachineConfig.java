package com.seu.monitor.config;

import java.util.HashMap;
import java.util.Map;

public class MachineConfig {
    public static final String firstMachineIdentifier = "01";

    public static final String[] sendMachineStatus = {"STOP","MANUAL","AUTORUN"};
    public static final Map SEND_MACHINE_STATUS_MAP = new HashMap(){{
        put("STOP",0);
        put("MANUAL",1);
        put("AUTORUN",2);
    }};

    public static final String disConnect = "离线";

    public static final String[] ReceiveMachineStatus = {
            "ERROR","STOP","MANUAL","AUTORUN","REMOTESTOP","REMOTERUN","REMOTEPOINT"
            //ERROR:设备故障		-1
            //STOP：停止		0
            //MANUAL：手动		1
            //AUTORUN：自动运行		2
            //REMOTESTOP:远程停止		3
            //REMOTERUN:远程运行		4
            //REMOTEPOINT:远程点动		5
    };
}
