package com.ry.workbench.dao;

import com.ry.workbench.pojo.Contacts;
import com.ry.workbench.pojo.Tran;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface TranDao {
    //保存一条交易记录
    int save(Tran tran);
    //多条件查询
    List<Tran> pageList(@Param("tran") Tran tran,@Param("pageNo") Integer pageNo,@Param("pageSize") Integer PageSize);
    //获取数量
    int getTranCount(Tran tran);
    //获取联系人列表 交易添加页
    List<Contacts> getConvertModelContactList(@Param("name") String name);
    //获取详细信息
    Tran detail(String id);
    //更新交易
    int updateTran(Tran tran);
    //跳转详情页信息
    Tran getDetail(String id);
    //修改状态
    int changeStage(Tran tran);
    //获取总条数
    int getTotal();
    //获取列表 阶段 和 条数
    List<Map<String, Object>> getDateList();
}
