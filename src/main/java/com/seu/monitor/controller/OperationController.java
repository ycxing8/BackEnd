package com.seu.monitor.controller;

import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.Operation;
import com.seu.monitor.entity.User;
import com.seu.monitor.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/operation")
public class OperationController {

    @Autowired
    private OperationRepository operationRepository;

    @PostMapping(value = "/get_operation_by_user")
    public List<Operation> getOperationByUser(@RequestParam("user_name")String userName, HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.ADMIN)){
            return new ArrayList<>();
        }else {
            return operationRepository.findByUserName(userName);
        }
    }

}