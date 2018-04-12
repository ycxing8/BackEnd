package com.seu.monitor.controller;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.Machine;
import com.seu.monitor.entity.Operation;
import com.seu.monitor.repository.MachineRepository;
import com.seu.monitor.repository.OperationRepository;
import com.seu.monitor.socket.SendFormChangeThread;
import com.seu.monitor.utils.MachineUtils;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/machine")
public class MachineController {

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private OperationRepository operationRepository;

    @PostMapping(value = "/control_by_word")
    public String controlByWord(@RequestParam("identifier")String identifier,
    @RequestParam("control_word")String controlWord,
    HttpSession session){
        if(!session.getAttribute(UserConfig.USER_POWER).equals(UserConfig.ADMIN)){
            return "你没有足够权限！";
        }
        List<Machine> machineList = machineRepository.findByIdentifier(identifier);
        if(machineList.size() == 0){
            return "没有该设备！";
        }

        synchronized(SendFormChangeThread.messageListToMachine){
            controlWord = identifier + " " + controlWord;
            SendFormChangeThread.messageListToMachine.add(controlWord);
        }
        Operation operation = new Operation();
        operation.setUserName((String) session.getAttribute(UserConfig.USER_NAME));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        operation.setTime(dateFormat.format(new Date()));
        operation.setDescription(controlWord);
        operationRepository.save(operation);
        return "OK!";
    }

    //控制数据型如：11 K 50 ,是字符串形式的
    @PostMapping(value = "/control_by_short_word")
    public String controlByNum(@RequestParam("identifier")String identifier,
                                 @RequestParam("short_word")String shortWord,
                                 HttpSession session){
        if(!session.getAttribute(UserConfig.USER_POWER).equals(UserConfig.ADMIN)){
            return "你没有足够权限！";
        }
        List<Machine> machineList = machineRepository.findByIdentifier(identifier);
        if(machineList.size() == 0){
            return "没有该设备！";
        }
        String controlWord = null;
        try {
            int index = 0;
            String [] arr = shortWord.split("\\s+");
            for(String ss : arr){

                switch (index){
                    case 0:
                        int temp = Integer.parseInt(arr[0]);
                        controlWord = ComponentConfig.sendIdentifiers[temp - 1];
                        break;
                    case 1:
                        if(ss.equals("G")){
                            controlWord += " "+ "G";
                        }else if(ss.equals("K")){
                            controlWord += " "+ "K";
                        }else{
                            controlWord += " "+ "N";
                        }
                        break;
                    case 2:
                        controlWord += " " + ss;
                        break;
                }
                index++;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        synchronized(SendFormChangeThread.messageListToMachine){
            controlWord = identifier + " " + controlWord;
            SendFormChangeThread.messageListToMachine.add(controlWord);
        }
        Operation operation = new Operation();
        operation.setUserName((String) session.getAttribute(UserConfig.USER_NAME));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        operation.setTime(dateFormat.format(new Date()));
        operation.setDescription(controlWord);
        operationRepository.save(operation);
        return "OK!";
    }

    @PostMapping(value = "/add_machine")
    public String addMachine(@RequestParam("identifier")String identifier,
                             HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.ADMIN)){
            return "m没有足够权限！";
        }else{
            MachineUtils.createAMachine(identifier);
            return "OK!";
        }
    }

    @PostMapping(value = "/modify")
    public String modifyMachine(@RequestParam("identifier")String identifier,
                                @RequestParam("name")String name,
                                @RequestParam("place")String place,
                                @RequestParam("description")String description,
                                HttpSession session){

        if(UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.ADMIN)){
            return "没有足够权限！";
        }
        List<Machine> machinesList = machineRepository.findByIdentifier(identifier);
        if(machinesList.size() == 0) {
            return "没有这个设备！";
        }else{
            Machine machine = machinesList.get(0);
            machine.setName(name);
            machine.setPlace(place);
            machine.setDescription(description);
            machineRepository.save(machine);
            return "OK!";
        }
    }


    @GetMapping(value = "/get_all")
    public List<Machine> getAllMessage(HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.NORMAL_USER)){
            return null;
        }else{
            return machineRepository.findAll();
        }
    }

}
