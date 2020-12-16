package com.ry.workbench.web.controller;

import com.ry.settings.pojo.User;
import com.ry.settings.service.UserService;
import com.ry.utils.DateTimeUtil;
import com.ry.utils.UUIDUtil;
import com.ry.workbench.pojo.Contacts;
import com.ry.workbench.pojo.Tran;
import com.ry.workbench.pojo.TranHistory;
import com.ry.workbench.service.ActivityService;
import com.ry.workbench.service.ContactService;
import com.ry.workbench.service.CustomerService;
import com.ry.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("tran")
public class TranController {

    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("pageList.do")
    @ResponseBody
    public Object pageList(Tran tran,Integer pageNo,Integer pageSize){
        Map<String,Object> map = this.tranService.pageList(tran,pageNo,pageSize);
        return map;
    }

    @RequestMapping("add.do")
    public ModelAndView add(){

        ModelAndView mv = new ModelAndView();
        //查询出用户
        List<User> userList = userService.getUserList();
        mv.setViewName("/workbench/transaction/save");
        mv.addObject("userList",userList);
        return mv;
    }
    @RequestMapping("save.do")
    public String save(HttpServletRequest request,Tran tran, String customerName){
        tran.setId(UUIDUtil.getUUID());

        tran.setCreateBy(((User) request.getSession().getAttribute("user")).getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        Boolean re = this.tranService.save(tran,customerName);
        if (re){
            return "redirect:/workbench/transaction/index.jsp";
        }else{
            return "redirect:/workbench/transaction/save.jsp";
        }

    }
    @RequestMapping("getConvertModelContactList.do")
    @ResponseBody
    //查询线索转换也 联系人列表
    public Object getConvertModelContactList(String name){
        List<Contacts> list = this.tranService.getConvertModelContactList(name);
        return list;
    }


    @RequestMapping("getCustomerName.do")
    @ResponseBody
    //查询线索转换也 联系人列表
    public Object getCustomerName(String name){
        List<String> list = this.customerService.getCustomerName(name);
        return list;
    }



    @RequestMapping("edit.do")
    //修改界面
    public ModelAndView edit(String id){
        ModelAndView mv = new ModelAndView();
        //获取所有的用户名称
        List<User> userList = this.userService.getUserList();
        //获取当前交易的详细信息
        Tran tran = this.tranService.detail(id);
        //联系人名称
        String ContactName = this.contactService.getNameById(tran.getContactsId());
        //市场活动名称
        String activityName = this.activityService.getNameById(tran.getActivityId());
        //客户名称
        String customerName = this.customerService.getNameById(tran.getCustomerId());

        mv.addObject("userList",userList);
        mv.addObject("tran",tran);
        mv.addObject("ContactName",ContactName);
        mv.addObject("activityName",activityName);
        mv.addObject("customerName",customerName);
        mv.setViewName("/workbench/transaction/edit");
        return mv;
    }

    //修改操作
    @RequestMapping("updateTran.do")
    @ResponseBody
    public Object updateTran(HttpServletRequest request,Tran tran){
        String name = ((User)request.getSession().getAttribute("user")).getName();
        //修改人
        tran.setEditBy(name);
        //修改日期
        tran.setEditTime(DateTimeUtil.getSysTime());
        int re = this.tranService.updateTran(tran);
        return re;
    }
    //跳转详情页
    @RequestMapping("detail.do")
    public ModelAndView detail(HttpServletRequest request,String id){
        Tran re = this.tranService.getDetail(id);
        ModelAndView mv = new ModelAndView();

        /**
         * 处理可能性
         * 阶段t 阶段和可能性之间的关系 pmap
         */
        String stage = re.getStage(); //获取阶段
        //获取全局上下文对象
        ServletContext application = request.getSession().getServletContext();
        Map<String,String> map = (Map<String,String>)application.getAttribute("pMap");
        String possibilty = map.get(stage);
        re.setPossibilty(possibilty);

        mv.addObject("t",re);
        mv.setViewName("/workbench/transaction/detail");
        return mv;
    }

    //根据交易id取得相应历史列表
    @RequestMapping("getHistoryListByTranId.do")
    @ResponseBody
    public Object getHistoryListByTranId(HttpServletRequest request,String tranId){
        List<TranHistory> tranHistoryList = this.tranService.getHistoryListByTranId(tranId);
        //获取全局上下文对象
        ServletContext application = request.getSession().getServletContext();
        Map<String,String> map = (Map<String,String>)application.getAttribute("pMap");
        //循环遍历出每一条记录中的可能性字段
        for (TranHistory th:tranHistoryList) {
            //获取每一条的阶段
            String stage = th.getStage();
            th.setPossibilty(map.get(stage));
        }
        return  tranHistoryList;
    }
    //修改操作
    @RequestMapping("changeStage.do")
    @ResponseBody
    public Object changeStage(HttpServletRequest request,Tran tran){
        Map<String,Object> reMap = new HashMap<>();
        //修改人
        String name = ((User)request.getSession().getAttribute("user")).getName();
        tran.setEditBy(name);
        //修改日期
        tran.setEditTime(DateTimeUtil.getSysTime());

        Boolean flag = this.tranService.changeStage(tran);

        //获取全局上下文对象
        ServletContext application = request.getSession().getServletContext();
        Map<String,String> map = (Map<String,String>)application.getAttribute("pMap");
        //设置当前交易阶段对应的可能性
        tran.setPossibilty(map.get(tran.getStage()));
        reMap.put("success",flag);
        reMap.put("t",tran);

        return reMap;
    }

    //获取echarts图数据
    @RequestMapping("getCharts.do")
    @ResponseBody
    public Object getCharts(HttpServletRequest request){
        /**
         * 返回
         * total
         * dataList
         */
        Map<String,Object> re =  this.tranService.getCharts();
        return re;
    }

}
