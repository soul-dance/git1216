package com.ry.workbench.service;

import java.util.List;

public interface CustomerService {
    //获取所有客户的名称
    List<String> getCustomerName(String name);
    //根据id获取客户名称
    String getNameById(String customerId);
}
