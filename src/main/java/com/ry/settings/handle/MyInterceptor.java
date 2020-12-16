package com.ry.settings.handle;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      /*  boolean re = false;
        //获取请求的RUi:去除http:localhost:8080这部分剩下的
        String uri = request.getRequestURI();
        System.out.println(uri + "  是地址");
        //UTL:除了login.jsp是可以公开访问的，其他的URL都进行拦截控制
        System.out.println(uri.indexOf("/login") + "是indexof");
        if (uri.indexOf("/login") >= 0) {
            System.out.println("登录界面可以通过");
            return true;
        }
        if (request.getSession().getAttribute("user") != null){
            System.out.println("有id可以通过");
            re = true;
        }else{
            request.getRequestDispatcher("/index.jsp").forward(request,response);
        }*/
        System.out.println("pre拦截器执行了");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
