package com.ry.settings.dao;

import com.ry.settings.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
    //登录
    public User login(Map<String,String> user);
    //取得用户信息列表
    public List<User> getUserList();
}
