package com.ry.listener;

import com.ry.settings.dao.DicTypeDao;
import com.ry.settings.dao.DicValueDao;
import com.ry.settings.pojo.DicType;
import com.ry.settings.pojo.DicValue;
import com.ry.settings.service.DicService;
import com.ry.settings.service.impl.DicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysinitListener implements ServletContextListener {
    private static WebApplicationContext context;

    @Override
    public void contextInitialized(ServletContextEvent sce) {

        System.out.println("application 上下文加载开始");

        ServletContext application = sce.getServletContext();

        context = WebApplicationContextUtils.getWebApplicationContext(application);

        DicValueDao dicValueDao = (DicValueDao)context.getBean("dicValueDao");
        DicTypeDao dicTypeDao = (DicTypeDao)context.getBean("dicTypeDao");

        //将类型字典取出
        List<DicType> typeList = dicTypeDao.getTypeList();
        //遍历类型
        for (int i = 0; i < typeList.size(); i++){
            String code = typeList.get(i).getCode();

            List<DicValue> dvList = dicValueDao.getListByCode(code);

            application.setAttribute(code,dvList);
        }

        System.out.println("===数据字典处理完毕==");


        //将stage2properties文件解析
        /**
         * 将该属性文件中的键值对关系 处理成 java 中键值对关系
         *
         * 最终结果应该是一个map<String,String>
         *    Map<String(阶段),String(可能性)> map
         *
         *    map.put("审查",10)
         *    map.put("审议",20)
         *    .....
         *
         *    把map 放在服务器缓存中
         *    application.setAttribute()
         */
        //解析properties文件
        ResourceBundle bundle = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> keys = bundle.getKeys();
        Map<String,String> map = new HashMap<>();
        while (keys.hasMoreElements()){
            String key = keys.nextElement();
            String value = bundle.getString(key);
            System.out.println("key : " + key + "---- value : " +value);
            map.put(key,value);
        }
        application.setAttribute("pMap",map);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
