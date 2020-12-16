package com.ry.workbench.web.controller;


import com.ry.settings.pojo.User;
import com.ry.settings.service.UserService;
import com.ry.utils.DateTimeUtil;
import com.ry.utils.UUIDUtil;
import com.ry.workbench.pojo.Activity;
import com.ry.workbench.pojo.ActivityReamark;
import com.ry.workbench.service.ActivityService;
import com.ry.workbench.vo.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/myActivity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    /**
     * 获取用户列表
     * @return
     */
    @RequestMapping(value = "/getUserList.do",method = RequestMethod.GET)
    @ResponseBody
    public List<User> getUserList(){
        List<User> userList = this.userService.getUserList();
        return userList;
    }

    /**
     * 添加市场活动
     */
    @RequestMapping(value = "/save.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> save(HttpSession session, Activity activity){
        Map<String,Boolean> re = new HashMap<>();
        //提交人是当前登录的用户
        User user = (User)session.getAttribute("user");
        activity.setCreateBy(user.getName());
        int save = this.activityService.save(activity);
        if (save > 0){
            re.put("success",true);
        }else{
            re.put("success",false);
        }
        return re;
    }
    /**
     * 分页查询
     */
    @RequestMapping(value = "/pageList.do",method = RequestMethod.GET)
    @ResponseBody
    public Object pageList(HttpSession session,int pageNo,int pageSize,Activity activity){
        Map<String,Object> map = new HashMap<>();
        //返回一个总数量 一个activity集合
        //计算从第几条开始查 略过多少
        int skipCount = (pageNo-1)*pageSize;
        map.put("skipCount",skipCount); //略过多少
        map.put("pageSize",pageSize); //每页展示多少
        map.put("owner",activity.getOwner()); // 所有者
        map.put("startDate",activity.getStartDate()); //开始时间
        map.put("endDate",activity.getEndDate()); //结束时间
        map.put("name",activity.getName()); //项目名称
        /*
            前端要什么：
                市场活动列表
                记录总条数
         */
        Pagination<Activity> vo  = this.activityService.pageList(map);

        return vo;
    }
    /**
     * 删除市场活动操作
     */
    @RequestMapping(value = "/delete.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> deleteActivitys(HttpServletRequest request){
        String[] ids = request.getParameterValues("id");
        for(int i = 0;i <ids.length;i++){
            System.out.println(ids[i]);
        }
        Boolean re = this.activityService.delete(ids);
        Map<String,Boolean> map = new HashMap<>();
        map.put("success",re);
        return map;

    }
    /*
        查询用户列表和活动
     */
    @RequestMapping(value = "/getUserListAndActivity.do",method = RequestMethod.GET)
    @ResponseBody
    public Object getUserListAndActivity (String id){
        Map<String,Object> map = this.activityService.getUserListAndActivity(id);
        System.out.println(map);
        return map;
    }

    /**
     * 修改市场活动
     */
    @RequestMapping(value = "/updateActivity.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> updateActivity(HttpSession session, Activity activity){
        Map<String,Boolean> re = new HashMap<>();
        int count = this.activityService.updateActivity(activity);
        boolean flag = false;
        if (count > 0){
            flag = true;
        }
        re.put("success",flag);
        return  re;
    }
    /**
     * 查询该市场活动 详细信息 跳转详情页
     */
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ModelAndView detail(HttpServletRequest request, String id){
        System.out.println(id + "是市场活动id");
        //查详细信息
        Activity activity = this.activityService.detail(id);
        ModelAndView mv = new ModelAndView();
        mv.addObject("activity",activity);
        mv.setViewName("/workbench/activity/detail");
        return mv;
    }
    /**
     * 获取备注列表
     */
    @RequestMapping(value = "/getRemarkList.do",method = RequestMethod.POST)
    @ResponseBody
    public Object getRemarkList(String id){
        List<ActivityReamark> remarkList = this.activityService.getRemarkList(id);
        return remarkList;
    }
    /**
     * 删除备注
     */
    @RequestMapping(value = "/deleteReamark.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> deleteReamark(String id){
      Map<String,Boolean> re = new HashMap<>();
      boolean flag = false;
       int count = this.activityService.deleteReamark(id);
        System.out.println(count + "结果");
       if (count > 0){
            flag = true;
       }
       re.put("success",flag);
       return re;
    }
    /**
     * 添加备注
     */
    @RequestMapping(value = "/saveReamrk.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> saveReamrk(ActivityReamark activityReamark){
        System.out.println("controller进来了 参数：" + activityReamark);
        Map<String,Boolean> re = new HashMap<>();
        boolean flag = false;
        int count = this.activityService.saveReamrk(activityReamark);
        System.out.println(count + "结果");
        if (count > 0){
            flag = true;
        }
        re.put("success",flag);
        return re;
    }
    /**
     * 更新备注信息
     */
    @RequestMapping(value = "/updateReamrk.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> updateReamrk(ActivityReamark activityReamark){
        Map<String,Boolean> re = new HashMap<>();
        boolean flag = false;
        int count = this.activityService.updateReamrk(activityReamark);
        if (count > 0){
            flag = true;
        }
        re.put("success",flag);
        return re;
    }

}
