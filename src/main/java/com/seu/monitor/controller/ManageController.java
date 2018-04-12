package com.seu.monitor.controller;

import com.seu.monitor.config.UserConfig;
import com.seu.monitor.socket.SocketProcessThread;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/manage")
public class ManageController {

    @GetMapping(value = "/get_thread_connect_machine_status")
    public Map getThreadStatus(HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.ADMIN)){
            return new HashMap();
        }else{
            synchronized (SocketProcessThread.socketProcessThreadMap) {
                return SocketProcessThread.socketProcessThreadMap;
            }
        }
    }
}
