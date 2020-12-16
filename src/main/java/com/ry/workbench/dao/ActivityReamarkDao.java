package com.ry.workbench.dao;

import com.ry.workbench.pojo.ActivityReamark;

import java.util.List;

public interface ActivityReamarkDao {
    //删除留言
    int deleteByAids(String[] ids);
    //查询应该删除留言的条数
    int getCountByAids(String[] ids);

    List<ActivityReamark> getRemarkList(String id);

    int deleteReamark(String id);

    int saveReamrk(ActivityReamark activityReamark);

    int updateReamrk(ActivityReamark activityReamark);
}
