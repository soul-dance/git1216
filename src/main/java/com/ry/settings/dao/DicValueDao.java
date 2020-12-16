package com.ry.settings.dao;

import com.ry.settings.pojo.DicValue;
import com.ry.workbench.pojo.Clue;

import java.util.List;

public interface DicValueDao {
    //根据称呼获取list
    List<DicValue> getListByCode(String code);
    //保存
    int saveClue(Clue clue);
}
