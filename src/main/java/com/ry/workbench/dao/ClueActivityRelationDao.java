package com.ry.workbench.dao;


import com.ry.workbench.pojo.ClueActivityRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueActivityRelationDao {

    //查询线索id对应的所有市场活动
    List<ClueActivityRelation> getRelationById(String id);
    //添加市场活动联系表
    Integer addActivityBund(List<ClueActivityRelation> list);
    //根据id删除单条记录
    int delete(String id);
}
