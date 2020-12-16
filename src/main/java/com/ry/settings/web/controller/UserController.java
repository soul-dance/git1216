package com.ry.settings.web.controller;

import com.ry.exception.LoginException;
import com.ry.settings.pojo.User;
import com.ry.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/login.do")
    @ResponseBody
    public Map<String,String> login(HttpServletRequest request, User user) throws LoginException {
        StringBuffer url  = request.getRequestURL();
        System.out.println(url + "是我们的请求地址");
        System.out.println("进入到用户controller");
        //接受ip 地址
        String ip = request.getRemoteAddr();
        Map<String,String> map = new HashMap<>();
        User us = this.userService.login(user.getLoginAct(), user.getLoginPwd(), ip);
        //一旦程序执行到这里 说明程序执行成功,没有抛出异常,表示登录成功
        System.out.println("异常被解决了");
        request.getSession().setAttribute("user",us);
        map.put("success","true");

        return  map;
    }
}
