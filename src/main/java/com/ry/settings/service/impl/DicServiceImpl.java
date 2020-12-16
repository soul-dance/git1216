package com.ry.settings.service.impl;

import com.ry.settings.dao.DicTypeDao;
import com.ry.settings.dao.DicValueDao;
import com.ry.settings.pojo.DicType;
import com.ry.settings.pojo.DicValue;
import com.ry.settings.service.DicService;
import com.ry.workbench.pojo.Clue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DicServiceImpl implements DicService {

    @Resource
    private DicValueDao dicValueDao;
    @Resource
    private DicTypeDao dicTypeDao;


    /**
     * 获取全部的称呼
     * @return
     */
    @Override
    public Map<String, List<DicValue>> getAll() {

        Map<String,List<DicValue>> map = new HashMap<>();
        //将类型字典取出
        List<DicType> typeList = this.dicTypeDao.getTypeList();
        //遍历类型
        for (int i = 0; i < typeList.size(); i++){
            String code = typeList.get(i).getCode();
            List<DicValue> dvList = this.dicValueDao.getListByCode(code);
            System.out.println(dvList);
            map.put(code,dvList);
        }
        return map;
    }


}
