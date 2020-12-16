package com.ry.workbench.service;

import com.ry.workbench.pojo.Activity;
import com.ry.workbench.pojo.Clue;
import com.ry.workbench.pojo.ClueActivityRelation;
import com.ry.workbench.pojo.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    //保存线索
    Map<String, Boolean> saveClue(Clue clue);
    //多条件分页查询
    Map<String, Object> pageList(Integer pageNo, Integer pageSize, Clue clue);
    //详情页
    Clue detail(String id);
    //解除关联 根据id 删除掉市场线索关系表中的记录
    Map<String, Boolean> unbund(String id);
    //展示可以关联的市场活动列表
    List<Activity> getActivityBund(Activity activity);
    //添加市场活动关联
    Map<String, Boolean> addActivityBund(List<ClueActivityRelation> list);
    //查询线索转换页中市场活动列表
    List<Activity> getConvertModelActivityList(String name);
    //转换线索
    Boolean convert(String clueId, Tran tran, String creatBy);


}
