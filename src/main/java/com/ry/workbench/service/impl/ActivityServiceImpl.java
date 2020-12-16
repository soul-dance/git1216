package com.ry.workbench.service.impl;

import com.ry.settings.dao.UserDao;
import com.ry.settings.pojo.User;
import com.ry.utils.DateTimeUtil;
import com.ry.utils.UUIDUtil;
import com.ry.workbench.dao.ActivityDao;
import com.ry.workbench.dao.ActivityReamarkDao;
import com.ry.workbench.pojo.Activity;
import com.ry.workbench.pojo.ActivityReamark;
import com.ry.workbench.service.ActivityService;
import com.ry.workbench.vo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityReamarkDao activityReamarkDao;
    @Autowired
    private UserDao userDao;

    @Override
    /**
     * 添加
     */
    public int save(Activity activity) {
        int re = this.activityDao.save(activity);
        return  re;
    }

    @Override
    /**
     * 分页 多条件查询
     */
    public Pagination<Activity> pageList(Map<String, Object> map) {
        //取得total
        int total = this.activityDao.getTotalByCondition(map);
        //取得dataList
        List<Activity> list = this.activityDao.getActivityListCondition(map);

        //将total 和 datalist封装到vo中
        Pagination<Activity> vo = new Pagination<>();
        vo.setPageTotal(total);
        vo.setDataList(list);
        return vo;
    }

    @Override
    @Transactional
    public boolean delete(String[] ids) {
        boolean flag = false;
        //查询出需要删除备注的数量
        int count1 = this.activityReamarkDao.getCountByAids(ids);
        //删除备注 返回收到影响行数 去比对实际删除的数量
        int count2 = this.activityReamarkDao.deleteByAids(ids);
        //删除市场活动
        if (count1 == count2){
            int count3 =  this.activityDao.delete(ids);
            if (count3 == ids.length){
                flag = true;
            }
        }


        return flag;
    }

    @Override
    /**
     * 查询一个市场活动 和 用户信息列表
     */
    public Map<String, Object> getUserListAndActivity(String id) {
        Map<String,Object> map = new HashMap<>();
        //获取用户列表
        List<User> userList = this.userDao.getUserList();
        //获取单条市场活动
        Activity activity = this.activityDao.getActivityById(id);
        map.put("uList",userList);
        map.put("a",activity);
        return map;
    }

    @Override
    /**
     * 修改市场活动
     */
    public int updateActivity(Activity activity) {
        int re = this.activityDao.updateActivity(activity);
        return re;
    }

    @Override
    /**
     * 根据id查市场活动信息
     */
    public Activity detail(String id) {
        //获取市场活动信息
        Activity activity = this.activityDao.getActivity(id);
        //获取备注
        return activity;

    }

    @Override
    //获取当前市场活动留言列表
    public List<ActivityReamark> getRemarkList(String id) {
        List<ActivityReamark> activityReamarks = this.activityReamarkDao.getRemarkList(id);
        return activityReamarks;
    }

    @Override
    //删除备注
    @Transactional
    public int deleteReamark(String id) {
        System.out.println("进入到service中 id是：" + id);
        int re = this.activityReamarkDao.deleteReamark(id);
        return re;
    }

    @Override
    public int saveReamrk(ActivityReamark activityReamark) {
        //id
        String id = UUIDUtil.getUUID();
        //创建时间
        String creatTime =  DateTimeUtil.getSysTime();
        //状态
        String editFlag = "0";

        activityReamark.setId(id);
        activityReamark.setCreateTime(creatTime);
        activityReamark.setEditFlag(editFlag);

        int re  =  this.activityReamarkDao.saveReamrk(activityReamark);
        return re;
    }

    @Override
    /**
     * 修改备注信息
     */
    public int updateReamrk(ActivityReamark activityReamark) {
        /**
         * controller已经传过来 一个id 一个内容了 和 修改人了 还差一个修改时间
         */
        //状态
        String editFlag = "1";
        //修改时间
        String editTime =  DateTimeUtil.getSysTime();

        activityReamark.setEditFlag(editFlag);
        activityReamark.setEditTime(editTime);

        int re = this.activityReamarkDao.updateReamrk(activityReamark);
        return re;
    }

    /**
     * 获取市场关联列表
     * @return
     */
    @Override
    public List<Activity> getActivityListByCludId(String id) {

        //先根据线索id在第三张表中找到市场活动id 跟根据查到的市场活动id 去查每一个市场活动
        
        List<Activity> list = this.activityDao.getActivityListByCludId(id);

        return list;
    }

    @Override
    public String getNameById(String activityId) {
       String  name =  this.activityDao.getNameById(activityId);
        return name;
    }
}
