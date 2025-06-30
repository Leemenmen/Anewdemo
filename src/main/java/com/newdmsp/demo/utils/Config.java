package com.newdmsp.demo.utils;


import com.newdmsp.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

public class Config {

//     static Environment environment;

    public static String CURRENT_USERNAME = "loginUser";


//    public static String ROOT_PATH =  environment.getProperty("storage.root.path");
    public static String ROOT_PATH = "E:/dmsp/Anewdemo";


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
