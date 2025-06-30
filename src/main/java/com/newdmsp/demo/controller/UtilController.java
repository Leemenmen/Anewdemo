package com.newdmsp.demo.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



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
