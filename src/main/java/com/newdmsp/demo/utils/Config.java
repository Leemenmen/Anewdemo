package com.newdmsp.demo.utils;


import com.newdmsp.demo.entity.User;

import javax.servlet.http.HttpSession;

public class Config {

    public static String CURRENT_USERNAME = "loginUser";

//    public static String ROOT_PATH = "F:/project/Anewdemo/";
    public static String ROOT_PATH = "/opt/pubWebPro/";


    //Result
    public static int SUCCESS=200; //成功
    public static int UNSUCCESS=400;   //失败
    public static int ERROR=500;   //异常
    public static int Unauthorized=401; //成功
    public static int Forbidden=403;   //失败
    public static int NotFound=404;   //异常


    //启用自定义日志打印
    public static boolean ENABLE_CUSTOMEIZE_LOG = true;


    public static User getSessionUser(HttpSession session){
        return (User)session.getAttribute(CURRENT_USERNAME);
    }


}
