package com.ry.workbench.dao;

import com.ry.workbench.pojo.Activity;
import com.ry.workbench.pojo.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    //添加市场活动
    public int save(Activity activity);
    //得到市场活动总数
    public int getTotalByCondition(Map<String, Object> map);
    //获取全部市场活动
    public  List<Activity> getActivityListCondition(Map<String, Object> map);
    //删除单条市场活动
    public int delete(String[] ids);
    //获取单条市场活动
    public Activity getActivityById(String id);
    //修改市场活动
    public int updateActivity(Activity activity);
    //获取市场活动 与用户表关联 查出所有者
    public Activity getActivity(String id);
    //查询市场关联
    List<Activity> getActivityListByCludId(String id);
    //查询市场活动关联的列表 根据传来的活动id 不包括这些id的列表
    List<Activity> getActivityBund(@Param("name")String name,@Param("ids") List<ClueActivityRelation> ids);
    //查询线索转换页的市场活动列表
    List<Activity> getConvertModelActivityList(@Param("name") String name);

    String getNameById(String activityId);
}
