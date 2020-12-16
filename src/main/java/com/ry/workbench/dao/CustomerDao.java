package com.ry.workbench.dao;

import com.ry.workbench.pojo.Customer;

import java.util.List;

public interface CustomerDao {

    //根据公司名查询
    Customer selectByName(String company);
    //保存一个客户
    int save(Customer customer);
    //模糊查询 查询所有的客户名称
    List<String> getCustomerName(String name);
    //根据id获取名字
    String getNameById(String customerId);
}
