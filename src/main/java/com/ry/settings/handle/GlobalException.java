package com.ry.settings.handle;

import com.ry.exception.LoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalException {
    //定义方法
    /**
     * 处理异常的方法和控制器方法的定义一样 可以有ModelAndView String void Object
     */

    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public Map<String,String> Login(LoginException ex){
        System.out.println("进入到异常全局处理器");
        Map<String,String> map = new HashMap<>();
        System.out.println(ex.getMessage());

        ModelAndView mv = new ModelAndView();
        map.put("success","false");
        map.put("msg",ex.getMessage());
        System.out.println("异常处理器马上要结束了");
        System.out.println(map);
        return map;
    }
}
