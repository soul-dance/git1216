package com.ry.workbench.service.impl;

import com.ry.utils.DateTimeUtil;
import com.ry.utils.UUIDUtil;
import com.ry.workbench.dao.CustomerDao;
import com.ry.workbench.dao.TranDao;
import com.ry.workbench.dao.TranHistoryDao;
import com.ry.workbench.pojo.Contacts;
import com.ry.workbench.pojo.Customer;
import com.ry.workbench.pojo.Tran;
import com.ry.workbench.pojo.TranHistory;
import com.ry.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;
    @Autowired
    private CustomerDao customerDao;

    @Override
    public Map<String, Object> pageList(Tran tran, Integer pageNo, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        pageNo = (pageNo -1 )* pageSize;
        /**
         *  多条件查询交易列表
         */
        List<Tran> trans =  this.tranDao.pageList(tran,pageNo,pageSize);

        //查询总条数
        int count = this.tranDao.getTranCount(tran);

        map.put("tranList",trans);
        map.put("pageTotal",count);

        return map;
    }

    @Override
    /**
     *查询线索转换也 联系人列表
     */
    public List<Contacts> getConvertModelContactList(String name) {
        List<Contacts>  list =  this.tranDao.getConvertModelContactList(name);
        return list;
    }

    @Override
    /**
     * 获取交易详细信息·
     */
    public Tran detail(String id) {
        Tran tran = this.tranDao.detail(id);
        return tran;
    }

    @Override
    /**
     * 更新
     */
    @Transactional
    public int updateTran(Tran tran) {
        //更新交易记录
        int re = this.tranDao.updateTran(tran);
        //添加一条历史记录
        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        re = tranHistoryDao.save(th);
        return re;
    }

    @Override
    @Transactional
    public Boolean save(Tran tran, String customerName) {
        /**
         *  交易添加业务
         *      在做添加之前 tran 少了一条信息 就是客户的主键 customerId
         *  先处理客户相关的需求
         *      1.判断customerName 根据客户名称在客户表进行精确查询
         *          如果有,则取出这个客户的id,封装到t对象中
         *          如果没有,则在数据库新建一条
         *      2.经过以上操作 t对象信息就全了 需要执行添加 交易的都操作
         *
         *      3.添加交易后,需要添加一条历史交易记录
         */
        Boolean flag = true;
        //根据名字查询客户是否存在 如果没有则新建一个客户
        //2.通过线索对象提取客户信息 如果客户不存在 新建客户 根据公司名字精准匹配判断该客户是否存在
        Customer customer = this.customerDao.selectByName(customerName);
        if (customer == null){
            //如果customer 等于空 说明之前没有这个客户 需要新建
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            //添加客户
            int count1 =  customerDao.save(customer);
            if (count1 != 1){
                flag = false;
            }
        }
        //经过上面的操作 不论是查出来的用户 还是以前没有新增的用户 id都已经有了
        tran.setCustomerId(customer.getId());
        int count2 = this.tranDao.save(tran);
        if (count2  != 1 ){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getCreateBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        int count3 = tranHistoryDao.save(th);
        if (count3  != 1 ){
            flag = false;
        }
        return flag;
    }

    @Override
    /**
     * 跳转详情页
     */
    public Tran getDetail(String id) {
        Tran tran = this.tranDao.getDetail(id);
        return tran;
    }

    @Override
    /**
     * 根据交易id获取交易历史记录
     */
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> list =  this.tranHistoryDao.getHistoryListByTranId(tranId);
        return list;
    }

    @Override
    @Transactional
    public Boolean changeStage(Tran tran) {
        Boolean flag = true;
        //更改状态
        int count1 = this.tranDao.changeStage(tran);
        if (count1 != 1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(tran.getId());
        th.setStage(tran.getStage());
        th.setMoney(tran.getMoney());
        th.setExpectedDate(tran.getExpectedDate());
        th.setCreateBy(tran.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        int count2 = tranHistoryDao.save(th);
        if (count2  != 1 ){
            flag = false;
        }
        return flag;


    }

    /**
     * 获取交易图 数据
     * @return
     */
    @Override
    public Map<String, Object> getCharts() {

        //取得total
        int total = this.tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList =  this.tranDao.getDateList();
        System.out.println(dataList);
        //打包map
        Map<String,Object> map = new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        //返回map
        return map;
    }
}
