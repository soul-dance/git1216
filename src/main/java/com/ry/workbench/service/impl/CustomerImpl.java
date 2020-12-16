package com.ry.workbench.service.impl;

import com.ry.workbench.dao.CustomerDao;
import com.ry.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;
    /**
     * 获取所有的客户名称 模糊查询
     * @param name
     * @return
     */
    @Override
    public List<String> getCustomerName(String name) {
        List<String> list = this.customerDao.getCustomerName(name);
        return list;
    }

    @Override
    /**
     * 根据id获取名字
     */
    public String getNameById(String customerId) {
        String name = this.customerDao.getNameById(customerId);
        return name;
    }

}
