package com.ry.workbench.dao;

import com.ry.workbench.pojo.Contacts;

public interface ContactsDao {
    //保存一个联系人
    int save(Contacts contacts);

    String getNameById(String customerId);
}
