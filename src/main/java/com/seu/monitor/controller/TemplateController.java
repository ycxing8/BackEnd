package com.seu.monitor.controller;

import com.seu.monitor.config.UserConfig;
import com.seu.monitor.entity.User;
import com.seu.monitor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
public class TemplateController {

    @Autowired
    private UserRepository userRepository;


    @RequestMapping("/")
    public String index(){
        return "redirect:html/login.html";
    }

    @PostMapping(value = "/login")
    public String loginTest(@RequestParam("name")String name,
                            @RequestParam("password")String password,
                            HttpSession session,
                            ModelMap modelMap){

        List<User> userList = userRepository.findByName(name);
        int index = userList.size();
        if(index > 1){
            System.out.println("same name user num is " + index + ";" +
                    "system can't have two same name user!");
            return "redirect:html/login.html";//"该用户存在问题，请联系管理员！";
        }
        if(index == 0){
            return "redirect:html/login.html";//"账号错误，请检查账号！";
        }
        User user = userList.get(0);
        System.out.println(name + " password is " + user.getPassword() +", and input is " + password);
        if(user.getPassword().equals(password)){
            session.setAttribute(UserConfig.USER_POWER, user.getPower());
            session.setAttribute(UserConfig.USER_NAME, user.getName());
            modelMap.put("msg", "信息显示");
            return "/index";
            //return user.getName() + " login success;" + "you are " + user.getPower();
        }

        return "redirect:html/login.html";//"密码错误，请检查密码！";
    }

}
