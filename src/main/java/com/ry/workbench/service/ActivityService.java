package com.ry.workbench.service;

import com.ry.settings.pojo.User;
import com.ry.workbench.pojo.Activity;
import com.ry.workbench.pojo.ActivityReamark;
import com.ry.workbench.vo.Pagination;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    //添加市场活动
    public int save(Activity activity);
    //返回一个vo对象 里面有总条数 和 所有的市场活动列表
    public Pagination<Activity> pageList(Map<String, Object> map);
    //根据id 删除 市场活动
    public boolean delete(String[] ids);
    //获取用户列表和一个市场活动
    public  Map<String, Object> getUserListAndActivity(String id);
    //修改市场活动
    public int updateActivity(Activity activity);
    //获取详情市场信息
    public Activity detail(String id);
    //获取当前市场活动留言列表
    public List<ActivityReamark> getRemarkList(String id);

    public int deleteReamark(String id);

    public int saveReamrk(ActivityReamark activityReamark);

    int updateReamrk(ActivityReamark activityReamark);

    //获取关联市场活动
    List<Activity> getActivityListByCludId(String id);

    String getNameById(String contactsId);
}
