package com.ry.workbench.service;

import com.ry.workbench.pojo.Contacts;
import com.ry.workbench.pojo.Tran;
import com.ry.workbench.pojo.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    Map<String, Object> pageList(Tran tran, Integer pageNo, Integer pageSize);
    //查询线索转换也 联系人列表
    List<Contacts> getConvertModelContactList(String name);
    //获取当前交易详细信息
    Tran detail(String id);
    //更新
    int updateTran(Tran tran);
    //添加一条交易记录
    Boolean save(Tran tran, String customerName);
    //跳转详细页
    Tran getDetail(String id);
    //根据交易id 获取交易历史记录
    List<TranHistory> getHistoryListByTranId(String tranId);
    //更改状态
    Boolean changeStage(Tran tran);
    //获取echart数据 交易
    Map<String, Object> getCharts();
}
