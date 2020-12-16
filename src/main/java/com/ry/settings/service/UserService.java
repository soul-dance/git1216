package com.ry.settings.service;

import com.ry.exception.LoginException;
import com.ry.settings.pojo.User;

import java.util.List;

public interface UserService {
    //用户登录
    public User login(String loginAct,String loginPwd,String ip) throws LoginException;
    //取得用户信息列表
    public List<User> getUserList();

}
