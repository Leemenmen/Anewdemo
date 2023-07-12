package com.newdmsp.demo.security.user;

import com.newdmsp.demo.entity.Exp;
import com.newdmsp.demo.entity.Tgrade;
import com.newdmsp.demo.service.ExpService;
import com.newdmsp.demo.service.UserService;
import com.newdmsp.demo.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    UserService userService;
    @Resource
    ExpService expService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("登录成功");
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        log.info(String.valueOf(userDetails));
        //获取用户信息
        com.newdmsp.demo.entity.User user = userService.getUserInfo1(userDetails.getUsername());
//        log.info(String.valueOf(user));
        //返回json数据,并将数据存储在session中
        //处理编码方式，防止中文乱码的情况
        response.setContentType("application/json; charset=utf-8");
        //获取班级名称
        if (user.getRoleid()==1){
            Tgrade sgrade = userService.getStuGrade(user.getGradeid());
            user.setGname(sgrade.getGname());
            List<Exp> exps = expService.getExpsByGradeId(user.getGradeid());
            user.setExps(exps);
        }

//        log.info(String.valueOf(user));
//        session.setAttribute("loginUser", user);
        //将用户信息、当用户为学生时（班级名称），存入session中
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", user);

        //塞到HttpServletResponse中返回给前台
        response.getWriter().write("{\"code\":\"200\",\"roleId\":"+user.getRoleid()+"}");
    }

}
