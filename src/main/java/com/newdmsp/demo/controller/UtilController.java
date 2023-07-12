package com.newdmsp.demo.controller;


import com.newdmsp.demo.dao.ExpMapper;
import com.newdmsp.demo.entity.Exp;
import com.newdmsp.demo.entity.Tgrade;
import com.newdmsp.demo.entity.User;
import com.newdmsp.demo.service.ExpService;
import com.newdmsp.demo.service.UserService;
import com.newdmsp.demo.utils.Config;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class UtilController {


    @GetMapping(value = {"/", "/login"})
    public String loginPage() {
        return "login";
    }

    @GetMapping("/index")
    public String IndexPage() {
        return "index";
    }

    @GetMapping("/error")
    public String ErrorPage() {
        return "login";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
    @GetMapping("/lunbo")
    public String lunboPage() {
        return "lunbo";
    }

    @GetMapping("/figureTest")
    public String lunbo1Page() {
        return "figureTest";
    }



    @RequestMapping("/pages/{page}")
    public String toPage(@PathVariable String page) {
        return page.replace("_", "/");
    }



}
