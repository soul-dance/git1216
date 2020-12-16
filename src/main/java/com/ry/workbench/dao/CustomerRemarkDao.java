package com.ry.workbench.dao;

import com.ry.workbench.pojo.CustomerRemark;

public interface CustomerRemarkDao {
    //添加客户备注
    int save(CustomerRemark customerRemark);
}
