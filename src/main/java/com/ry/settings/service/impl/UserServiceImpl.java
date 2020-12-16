package com.ry.settings.service.impl;


import com.ry.exception.LoginException;
import com.ry.settings.dao.UserDao;
import com.ry.settings.pojo.User;
import com.ry.settings.service.UserService;
import com.ry.utils.DateTimeUtil;
import com.ry.utils.MD5Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User login(String loginAct, String loginPwd,String ip) throws LoginException {
        System.out.println("进到service ");
        Map<String,String> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd", MD5Util.getMD5(loginPwd));
        User user = this.userDao.login(map);
        if (user == null){
            //抛出 异常
            throw new LoginException("用户名密码错误");
        }
        //如果程序执行到这里 说明账号密码 正确
        //需要继续向下验证其他信息

        //验证失效时间
        String expireTime = user.getExpireTime(); //数据库失效信息
        String current = DateTimeUtil.getSysTime();//系统当前时间
        if (expireTime.compareTo(current) < 0){
            //账号失效
            throw new LoginException("账号失效");
        }

        //判断锁定状态
        String lockState = user.getLockState();
        if ("0".equals(lockState)){
            throw new LoginException("账号被锁定,请联系管理员");
        }

        //判断ip地址
        String allowIps = user.getAllowIps();
        if (!allowIps.contains(ip)){
            throw  new LoginException("ip地址受限");
        }


        return user;
    }

    @Override
    /**
     * 获取用户信息列表
     */
    public List<User> getUserList() {
        List<User> userList = this.userDao.getUserList();
        return  userList;
    }
}
