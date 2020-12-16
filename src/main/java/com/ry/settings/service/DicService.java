package com.ry.settings.service;

import com.ry.settings.pojo.DicValue;
import com.ry.workbench.pojo.Clue;

import java.util.List;
import java.util.Map;

public interface DicService {
    //获取全部
    Map<String, List<DicValue>> getAll();

}
