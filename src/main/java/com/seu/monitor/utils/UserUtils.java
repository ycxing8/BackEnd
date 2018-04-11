package com.seu.monitor.utils;

import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.User;
import com.seu.monitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UserUtils {
    @Autowired
    private UserRepository userRepository;

    public static UserUtils userUtils;
    @PostConstruct
    public void init() {
        userUtils = this;
        userUtils.userRepository = this.userRepository;
    }

    public static boolean addRootUser(){
        if(userUtils.userRepository.findByName(UserConfig.ROOT_USER).size() > 0){
            System.out.println("root user already exist.");
            return false;
        }
        User user = new User();
        user.setName(UserConfig.ROOT_USER);
        user.setPassword(UserConfig.ROOT_PASSWORD);
        user.setPower(UserConfig.ADMIN);
        user.setMachineIdentifiers(UserConfig.ALL_MACHINE);
        userUtils.userRepository.save(user);
        return true;
    }


}
