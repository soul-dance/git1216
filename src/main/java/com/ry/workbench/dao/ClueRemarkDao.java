package com.ry.workbench.dao;

import com.ry.workbench.pojo.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {
    //根据clueId查询与该id关联的备注列表
    List<ClueRemark> getListByClueId(String clueId);
    //删除单条
    int delete(String id);
}
