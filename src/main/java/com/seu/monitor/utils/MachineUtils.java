package com.seu.monitor.utils;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.entity.Machine;
import com.seu.monitor.repository.ComponentRepository;
import com.seu.monitor.repository.MachineRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.List;

@Component
public class MachineUtils {

    @Autowired
    private MachineRepository machineRepository;

    public static MachineUtils machineUtils;

    @PostConstruct
    public void init() {
        machineUtils = this;
        machineUtils.machineRepository = this.machineRepository;
    }

    public static Machine findByMachineIdentityCode(String identityCode){
        //System.out.println(machineUtils.machineRepository);//for test
        //System.out.println(machineUtils.machineRepository.findByMachineIdentityCode(identityCode));
        List<Machine> machineList = machineUtils.machineRepository.findByIdentityCode(identityCode);
        if(machineList.size() == 0){
            return null;
        } else{
            return machineList.get(0);//取最上面的一个，且相同identity code 相同的machine应只有一个
        }
    }

    public static boolean ifMachine(String identifier){
        List<Machine> machineList = machineUtils.machineRepository.findByIdentifier(identifier);
        if(machineList.size() > 0){
            return true;
        }
        return false;
    }

    public static void createFirstMachine(){
        if(machineUtils.machineRepository.findAll().size() == 0){
            Machine machine = new Machine();
            machine.setIdentifier(MachineConfig.firstMachineIdentifier);
            machine.setIdentityCode(DigestUtils.md5Hex(MachineConfig.firstMachineIdentifier));
            machineUtils.machineRepository.save(machine);
            ComponentUtils.addAMachineComponent(MachineConfig.firstMachineIdentifier);
        }else {
            System.out.println("First machine already exist.");
        }

    }


    public static boolean changeStatus(String identifier,String status){
        if(machineUtils.machineRepository.findByIdentifier(identifier).size() > 0){
            Machine machine = machineUtils.machineRepository.findByIdentifier(identifier).get(0);
            machine.setStatus(status);
            machineUtils.machineRepository.save(machine);
            return true;
        }
        System.out.println("this machine do not exist.");
        return false;
    }



}
