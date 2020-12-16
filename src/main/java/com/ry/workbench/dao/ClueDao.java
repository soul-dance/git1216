package com.ry.workbench.dao;


import com.ry.workbench.pojo.Clue;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    //保存线索
    int saveClue(Clue clue);
    //查询线索列表 根据条件 分页
    List<Clue> searchClueList( Map<String,Object> map);
    //查询总条数
    int getClueListCount(Map<String,Object> map);
    //详情页
    Clue detail(String id);
    //根据id删除市场活动和线索关系表中的一条数据
    Integer unbund(String id);
    //根据id查单条
    Clue getById(String clueId);
    //删除单条线索记录根据id
    int delete(String clueId);
}
