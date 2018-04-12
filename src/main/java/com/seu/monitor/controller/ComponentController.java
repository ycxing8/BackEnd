package com.seu.monitor.controller;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.Component;
import com.seu.monitor.entity.composite.ComponentCompositeKey;
import com.seu.monitor.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping(value = "/api/component")
public class ComponentController {

    @Autowired
    private ComponentRepository componentRepository;

    @PostMapping(value = "/add")
    public String add(@RequestParam("machine_identifier")String machineIdentifier,
                      @RequestParam("identifier")String identifier,
                      HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.ADMIN)){
            return "没有足够权限！";
        }
        com.seu.monitor.entity.Component component = new com.seu.monitor.entity.Component();
        ComponentCompositeKey componentCompositeKey = new ComponentCompositeKey();
        componentCompositeKey.setMachineIdentifier(machineIdentifier);
        componentCompositeKey.setIdentifier(identifier);
        component.setPk(componentCompositeKey);
        componentRepository.save(component);
        return "OK!";
    }

    @PostMapping(value = "/modify")
    public String modify(@RequestParam("machine_identifier")String machineIdentifier,
                      @RequestParam("identifier")String identifier,
                      @RequestParam("kind")String kind,
                      @RequestParam("name")String name,
                      @RequestParam("description")String description,
                      HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),
                UserConfig.ADMIN)){
            return "没有足够权限！";
        }
        List<Component> componentList = componentRepository.findByPkMachineIdentifierAndPkIdentifier(machineIdentifier, identifier);
        if(componentList.size() == 0){
            return "没有这个组件！";
        }
        Component component = componentList.get(0);
        component.setKind(kind);
        component.setName(name);
        component.setDescription(description);
        componentRepository.save(component);

        return "OK!";
    }



    @PostMapping(value ="/get_by_machine_identifier")
    public List<Component> getByMachineIdentifier(@RequestParam("machine_identifier")String machineIdentifier,
                                                  HttpSession session){
        if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),UserConfig.NORMAL_USER)){
            //user’s power is accord with need power
            return new ArrayList<>();
        }
        List<Component> componentList = componentRepository.findByPkMachineIdentifier(machineIdentifier);
        Collections.sort(componentList, new Comparator<Component>(){
            @Override
            public int compare(Component c1, Component c2) {
                return c1.getId() - c2.getId();
            }
        });
        return componentList;
    }

    @GetMapping(value = "/get_all")
    public List<Component> getAllComponentMessage(HttpSession session){

       if(!UserConfig.verifyUserPower((String)session.getAttribute(UserConfig.USER_POWER),UserConfig.NORMAL_USER)){
          //user’s power is accord with need power
           return new ArrayList<Component>();
       }
       return componentRepository.findAll();
    }

}
