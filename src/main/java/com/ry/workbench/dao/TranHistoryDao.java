package com.ry.workbench.dao;

import com.ry.workbench.pojo.TranHistory;

import java.util.List;

public interface TranHistoryDao {
    //添加一条交易历史记录
    int save(TranHistory th);
    // 根据交易id获取交易历史记录
    List<TranHistory> getHistoryListByTranId(String tranId);
}
