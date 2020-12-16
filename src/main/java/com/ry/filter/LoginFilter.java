package com.ry.filter;

import com.ry.settings.pojo.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("进入验证有没有登录的过滤器");
        //请求对象
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        //响应对象
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        //获取浏览器请求地址
        String path =request.getRequestURI();
        if (path.indexOf("/login.jsp") > 0 || path.indexOf("/login.do") > 0){
            //登录操作 放行
            filterChain.doFilter(servletRequest,servletResponse);
        }else{
            //session
            HttpSession session = request.getSession();
            //获取session中user
            User user = (User) session.getAttribute("user");
            if (user != null){
                //登录状态 放行
                filterChain.doFilter(servletRequest,servletResponse);
                System.out.println("放行");
            }else{
                //非登录状态
                response.sendRedirect(request.getContextPath()+"/login.jsp");
            }
        }


    }

    @Override
    public void destroy() {

    }

}
