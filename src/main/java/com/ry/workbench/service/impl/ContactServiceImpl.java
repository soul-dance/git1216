package com.ry.workbench.service.impl;

import com.ry.workbench.dao.ContactsDao;
import com.ry.workbench.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactsDao contactsDao;
    @Override
    public String getNameById(String customerId) {
        String name = this.contactsDao.getNameById(customerId);
        return name;
    }
}
