package com.ry.workbench.service.impl;

import com.ry.utils.DateTimeUtil;
import com.ry.utils.UUIDUtil;
import com.ry.workbench.dao.*;
import com.ry.workbench.pojo.*;
import com.ry.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClueServiceImpl implements ClueService {

    //线索相关表
    @Autowired
    private ClueDao clueDao;
    @Autowired
    private ClueActivityRelationDao clueActivityRelationDao;
    @Autowired //线索备注
    private ClueRemarkDao clueRemarkDao;

    //客户相关表
    @Autowired //客户表
    private CustomerDao customerDao;
    @Autowired //客户备注表
    private CustomerRemarkDao customerRemarkDao;

    //联系人相关表
    @Autowired //联系人表
    private ContactsDao contactsDao;
    @Autowired //联系人备注表
    private ContactsRemarkDao contactsRemarkDao;
    @Autowired //联系人活动关系表
    private ContactsActivityRelationDao contactsActivityRelationDao;

    //交易相关表
    @Autowired //交易表
    private TranDao tranDao;
    @Autowired //交易历史表
    private TranHistoryDao tranHistoryDao;

    @Autowired
    private ActivityDao activityDao;

    @Override
    public Map<String, Boolean> saveClue(Clue clue) {
        Map<String,Boolean> map = new HashMap<>();

        int re = this.clueDao.saveClue(clue);

        if (re > 0){
            map.put("success",true);
        }
        return map;
    }

    @Override
    @Transactional
    public Map<String, Object> pageList(Integer pageNo, Integer pageSize, Clue clue) {

        //根据分页查出 相应记录 再 查出总记录数 放到map中
        Map<String,Object> map = new HashMap<>();

        //查询线索列表
        Map<String,Object> parmMap = new HashMap<>();
        pageNo = (pageNo-1)*pageSize;
        parmMap.put("pageNo",pageNo);
        parmMap.put("pageSize",pageSize);
        parmMap.put("fullname",clue.getFullname());
        parmMap.put("owner",clue.getOwner());
        parmMap.put("company",clue.getCompany());
        parmMap.put("phone",clue.getPhone());
        parmMap.put("mphone",clue.getMphone());
        parmMap.put("state",clue.getState());
        parmMap.put("source",clue.getSource());
        List<Clue> clueList = this.clueDao.searchClueList(parmMap);
        //查询总条数
        int total = this.clueDao.getClueListCount(parmMap);
        map.put("clueList",clueList);
        map.put("pageTotal",total);

        return map;
    }

    /**
     * 详情页
     * @param id
     * @return
     */
    @Override
    public Clue detail(String id) {
        Clue clue = this.clueDao.detail(id);
        return clue;
    }

    @Override
    /**
     * 根据id删除市场线索关系表中的一条记录
     */
    public Map<String, Boolean> unbund(String id) {
        Map<String,Boolean> map = new HashMap<>();
        Integer re = this.clueDao.unbund(id);
        if (re > 0){
            map.put("success",true);
        }
        return map;
    }

    /**
     * 展示可以关联的市场活动列表
     * @param activity
     * @return
     */
    @Override
    @Transactional
    public List<Activity> getActivityBund(Activity activity) {
        //先根据线索id查到该线索关联的市场活动
        List<ClueActivityRelation> list = this.clueActivityRelationDao.getRelationById(activity.getId());
        System.out.println(list + "是结果");
        //再去市场活动表中查询 不包括上述结果的市场活动列表列表
        List<Activity> activityList = this.activityDao.getActivityBund(activity.getName(),list);
        return activityList;
    }

    @Override
    /**
     * 添加市场活动关联
     */
    @Transactional
    public Map<String, Boolean> addActivityBund(List<ClueActivityRelation> list) {

        Map<String,Boolean> reMap = new HashMap<>();
        Integer re = this.clueActivityRelationDao.addActivityBund(list);
        if (re > 0){
            reMap.put("success",true);
        }
        return reMap;
    }

    /**
     * 查询线索页市场活动列表
     * @param name
     * @return
     */
    @Override
    public List<Activity> getConvertModelActivityList(String name) {
        List<Activity> list  =  this.activityDao.getConvertModelActivityList(name);
        return list;
    }

    @Override
    /**
     * x线索转换
     */
    @Transactional
    public Boolean convert(String clueId, Tran tran, String creatBy) {
        System.out.println(tran);
        Boolean re = true;
        String creatTime = DateTimeUtil.getSysTime();

        //1.通过线索id获取到 线索对象
        Clue clue = this.clueDao.getById(clueId);

        //2.通过线索对象提取客户信息 如果客户不存在 新建客户 根据公司名字精准匹配判断该客户是否存在
        String company =  clue.getCompany(); //公司名
        Customer customer = customerDao.selectByName(company);
        if (customer == null ){
            //如果customer 等于空 说明之前没有这个客户 需要新建
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setPhone(clue.getPhone());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime()); //下次联系时间
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setCreateTime(creatTime);  //创建时间
            customer.setCreateBy(creatBy); //创建人
            customer.setContactSummary(clue.getContactSummary());  //纪要
            //添加客户
            int count1 =  customerDao.save(customer);
            if (count1 != 1){
                re = false;
            }
        }


        //------------------------------------------------------------------------
        //经过第二步处理后客户的信息我们已经拥有了,将来在处理其他表时要用到客户的id
        //直接使用customer.getId()
        //------------------------------------------------------------------------

        //3.通过线索对象提取联系人信息，保存联系人
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setSource(clue.getSource());//来源
        contacts.setOwner(clue.getOwner());
        contacts.setNextContactTime(clue.getNextContactTime()); //下次联系时间
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCreateTime(creatTime);
        contacts.setBirth("");
        contacts.setCreateBy(creatBy);
        contacts.setCustomerId(customer.getId());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
        //添加联系人
        int count2 = contactsDao.save(contacts);
        if (count2 != 1){
            re = false;
        }



        //------------------------------------------------------------------------
        //通过第三步的处理 联系人的信息已经有了
        //如果以后要是用到联系人id 直接使用contacts.getId()
        //------------------------------------------------------------------------

        //4.将线索备注转到联系人备注和客户备注
        //查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList =  clueRemarkDao.getListByClueId(clueId);
        //取出每一条线索备注
        for (ClueRemark clueRemark:clueRemarkList) {
            //取出备注信息(主要转换到客户备注和联系人备注)

            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象 添加客户备注
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(creatBy);
            customerRemark.setCreateTime(creatTime);
            customerRemark.setCustomerId(customer.getId());
            customerRemark.setEditBy("0");
            customerRemark.setNoteContent(noteContent);
            //添加客户备注
            int count3 = this.customerRemarkDao.save(customerRemark);
            if (count3 != 1){
                re = false;
            }
            //创建联系人备注对象添加联系人
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(creatBy);
            contactsRemark.setCreateTime(creatTime);
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setEditBy("0");
            contactsRemark.setNoteContent(noteContent);
            //添加联系人备注
            int count4 = this.contactsRemarkDao.save(contactsRemark);
            if (count4 != 1){
                re = false;
            }
        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //根据线索id可以获取所有对应的市场活动
        List<ClueActivityRelation> relationById = this.clueActivityRelationDao.getRelationById(clueId);
        //获取每一条市场活动id
        for (ClueActivityRelation c:relationById) {
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(c.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                int count5 = this.contactsActivityRelationDao.save(contactsActivityRelation);
                if (count5 != 1){
                    re = false;
                }
        }

        //(6) 如果有创建交易需求，创建一条交易
        if (tran.getId() != null){
            //有交易需求 创建一条交易
            /**
             * tran 在controller 中已经封装了一部分信息
             * id money name expecteDate stage activityId creatBy creatTime
             */
            System.out.println(tran);
            tran.setSource(contacts.getSource());
            tran.setOwner(contacts.getOwner());
            tran.setNextContactTime(contacts.getNextContactTime());
            tran.setDescription(contacts.getDescription());
            tran.setCustomerId(customer.getId());
            tran.setContactSummary(contacts.getContactSummary());
            tran.setContactsId(contacts.getId());

            //添加交易记录
            int count6 = this.tranDao.save(tran);
            if (count6 != 1){
                re =false;
            }

            //7.如果创建了交易 则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateTime(creatTime);
            th.setCreateBy(creatBy);
            th.setExpectedDate(tran.getExpectedDate());
            th.setMoney(tran.getMoney());
            th.setStage(tran.getStage());
            th.setTranId(tran.getId());
            //添加交易历史
            int count7 =  this.tranHistoryDao.save(th);
            if (count7 != 1){
                re = false;
            }
        }

        //8.删除备注列表
        for (ClueRemark clueRemark:clueRemarkList) {

            int count8 = clueRemarkDao.delete(clueRemark.getId());
            if (count8 != 1){
                re = false;
            }
        }

        //9.删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation:relationById) {
                int count9 = clueActivityRelationDao.delete(clueActivityRelation.getId());
                if (count9 != 1){
                    re = false;
                }
        }

        //10.删除线索·
        int count10 = this.clueDao.delete(clueId);
        if (count10 != 1){
            re = false;
        }

        return re;
    }


}
