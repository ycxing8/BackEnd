package com.seu.monitor.utils;

import com.seu.monitor.config.ComponentConfig;
import com.seu.monitor.config.MachineConfig;
import com.seu.monitor.entity.ComponentLog;
import com.seu.monitor.entity.composite.ComponentCompositeKey;
import com.seu.monitor.repository.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.util.List;

@Component
public class ComponentUtils {

    @Autowired
    private ComponentRepository componentRepository;
    public static ComponentUtils componentUtils;

    @PostConstruct
    public void init() {
        componentUtils = this;
        componentUtils.componentRepository = this.componentRepository;
    }

    public static boolean modifyComponentRealTimeData(ComponentLog componentLog) {

        //System.out.println("You run here!");//for test
        //System.out.println(componentLog.getComponentIdentifier());
        //System.out.println(componentLog.getData());
        //System.out.println(componentUtils.componentRepository);

        List<com.seu.monitor.entity.Component> componentList =
                componentUtils.componentRepository.findByPkMachineIdentifierAndPkIdentifier(
                        componentLog.getMachineIdentifier(),componentLog.getComponentIdentifier());

        //System.out.println("You have accessed database!");//for test
        //System.out.println(componentLog.getComponentIdentifier() +" this machine num is: " +componentList.size());
        //System.out.println(componentList);
        if(componentList != null && componentList.size() == 0){
            com.seu.monitor.entity.Component component =
                    new com.seu.monitor.entity.Component();
            ComponentCompositeKey componentCompositeKey = new ComponentCompositeKey();
            componentCompositeKey.setIdentifier(componentLog.getComponentIdentifier());
            componentCompositeKey.setMachineIdentifier(componentLog.getMachineIdentifier());
            component.setPk(componentCompositeKey);
            component.setRealTimeData(componentLog.getData());
            component.setStatus(componentLog.getStatus());
            component.setUnit(componentLog.getUnit());
            componentUtils.componentRepository.save(component);
            return true;

        }else if(componentList != null && componentList.size() == 1){
            com.seu.monitor.entity.Component component = componentList.get(0);
            component.setRealTimeData(componentLog.getData());
            component.setStatus(componentLog.getStatus());
            component.setUnit(componentLog.getUnit());
            componentUtils.componentRepository.save(component);
            return true;
        }
        return false;
    }


    //没有检查这个设备的组件是否已经添加，可能存在困扰
    public static boolean addAMachineComponent(String machineIdentifier){

        //设备MACHINE项不加入component表
        for(Integer i = 0; i < ComponentConfig.componentIdentifiers.length; i++){
            com.seu.monitor.entity.Component component = new com.seu.monitor.entity.Component();
            ComponentCompositeKey componentCompositeKey = new ComponentCompositeKey();
            componentCompositeKey.setMachineIdentifier(machineIdentifier);
            componentCompositeKey.setIdentifier(ComponentConfig.componentIdentifiers[i]);
            component.setPk(componentCompositeKey);
            component.setId(i + 1);
            component.setName(ComponentConfig.componentNames[i]);
            component.setKind(getKind(i));

            List<com.seu.monitor.entity.Component> componentList =
                    componentUtils.componentRepository.findByPkMachineIdentifierAndPkIdentifier(
                            MachineConfig.firstMachineIdentifier,ComponentConfig.componentIdentifiers[i]);
            if(componentList.size() > 0){
                component.setKind(componentList.get(0).getKind());
                component.setName(componentList.get(0).getName());
                //component.setDescription(componentList.get(0).getDescription());
                component.setUnit(componentList.get(0).getUnit());
            }
            componentUtils.componentRepository.save(component);
        }
        return true;
    }

    private static String getKind(int index){
        for(Integer i = 0; i < ComponentConfig.componentNum.length; i++){
            if(index < ComponentConfig.componentNum[i]) {
                return ComponentConfig.componentKinds[i];
            }
        }

        return ComponentConfig.defaultKind;
    }

}
