package com.ry.setting.test;

import com.ry.utils.DateTimeUtil;
import com.ry.utils.MD5Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test1 {

    public static void main(String[] args) {
        //验证失效时间
       // String extimer = "2019-10-10 10:10:10";

        //和当前系统时间去比
/*        String time = DateTimeUtil.getSysTime();
        int i = extimer.compareTo(time);
        System.out.println(i > 0 ? "没有超时" : "超时了");*/

        String pwd = "123";
        String md5 = MD5Util.getMD5(pwd);
        System.out.println(md5);
        String md51 = MD5Util.getMD5(md5);
        System.out.println(md51);
    }
}
