package com.ry.workbench.web.controller;

import com.ry.settings.pojo.User;
import com.ry.settings.service.UserService;
import com.ry.utils.DateTimeUtil;
import com.ry.utils.UUIDUtil;
import com.ry.workbench.pojo.Activity;
import com.ry.workbench.pojo.Clue;
import com.ry.workbench.pojo.ClueActivityRelation;
import com.ry.workbench.pojo.Tran;
import com.ry.workbench.service.ActivityService;
import com.ry.workbench.service.ClueService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/clue")
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ActivityService activityService;



    /**
     * 获取所有用户的称谓 用来获取用户下拉框数据
     * @return
     */
    @ResponseBody
    @RequestMapping("/getUserList.do")
    public Object getUserList(){
        List<User> userList = this.userService.getUserList();
        return userList;
    }

    /**
     * 添加一条记录
     * @param clue
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/save.do")
    public Object saveClue(Clue clue, HttpServletRequest request){

        User user = (User)request.getSession().getAttribute("user");
        clue.setId(UUIDUtil.getUUID()); //线索id
        clue.setCreateBy(user.getName());//创建人
        clue.setCreateTime(DateTimeUtil.getSysTime()); //创建时间
        //获取map
        Map<String,Boolean> map = this.clueService.saveClue(clue);
        return map;
    }
    /**
     * 分页多条件查询
     * @param pageNo
     * @param pageSize
     * @param clue
     * @return
     */
    @ResponseBody
    @RequestMapping("/pageList.do")
    public Object pageList(Integer pageNo, Integer pageSize ,Clue clue){
        System.out.println(clue);
        Map<String,Object> map = this.clueService.pageList(pageNo,pageSize,clue);
        return map;
    }
    /**
     * 详情页
     */
    @RequestMapping(value = "/detail.do",method = RequestMethod.GET)
    public ModelAndView detail(String id){
        ModelAndView mv = new ModelAndView();
        Clue clue = this.clueService.detail(id);
        mv.addObject("clue",clue);
        mv.setViewName("/workbench/clue/detail");

        return mv;
    }
    /**
     * 查询市场活动关联列表
     */
    @ResponseBody
    @RequestMapping(value = "/getActivityListByCludId.do")
    public Object getActivityListByCludId(String id){
        List<Activity> list = this.activityService.getActivityListByCludId(id);
        return list;
    }
    /**
     * 解除关联 删除关联表的这条记录
     */
    @ResponseBody
    @RequestMapping("/unbund.do")
    public Object unbund(String id){
        Map<String,Boolean> map = this.clueService.unbund(id);
        return  map;
    }

    /**
     * 展示可以关联的市场活动列表
     */
    @ResponseBody
    @RequestMapping("/getActivityBund.do")
    public Object getActivityBund(Activity activity){
        //这里的id实际上是线索id
        List<Activity> list = this.clueService.getActivityBund(activity);
        return list;
    }


    /**
     * 添加关联市场活动
     */
    @ResponseBody
    @RequestMapping("/addActivityBund.do")
    public Object addActivityBund(HttpServletRequest request,@Param("activityId") String activityId, @Param("clueId") String clueId){
        System.out.println(activityId);
        String [] activityIds = activityId.split(",");
        List<ClueActivityRelation> list = new ArrayList<>();

        for(int i = 0; i < activityIds.length; i++){
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(activityIds[i]);
            clueActivityRelation.setClueId(clueId);
            System.out.println(clueActivityRelation);
            list.add(clueActivityRelation);
        }
        Map<String,Boolean> map = this.clueService.addActivityBund(list);
        return map;
    }

    /**
     * 展示可以关联的市场活动列表
     */
    @ResponseBody
    @RequestMapping("/getConvertModelActivityList.do")
    public Object getConvertModelActivityList(String name){
        System.out.println(name);
        List<Activity> list = this.clueService.getConvertModelActivityList(name);
        return list;
    }

    /**
     * 转换线索
     */
    @RequestMapping("/convert.do")
    public String convert(HttpServletRequest request,String clueId, Tran tran,String flag){
        String creatBy = ((User)request.getSession().getAttribute("user")).getName();
        if ("a".equals(flag)){
            //创建交易
            System.out.println(tran);
            //创建人
            tran.setCreateBy(creatBy);
            //创建时间
            tran.setCreateTime(DateTimeUtil.getSysTime());
            //UUID
            tran.setId(UUIDUtil.getUUID());
        }
        /**
         * 为业务层传递参数
         * 1.clueId 有了clueId才知道转换那一条线索记录
         * 2.在线索转换过程中,有可能会临时创建一笔交易  (业务层接受的t也有可能是个null)
         *
         */
        Boolean re = this.clueService.convert(clueId,tran,creatBy);
        if (re){
        ModelAndView mv = new ModelAndView();
//        mv.setViewName("redirect:/workbench/clue/index");
            return "redirect:/workbench/clue/index.jsp";
        }else {
            return "redirect:/workbench/clue/detail.jsp";
        }

    //        return  mv;
    }
}
