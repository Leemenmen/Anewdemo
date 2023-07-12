package com.newdmsp.demo.controller;


import com.newdmsp.demo.entity.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@Api(tags = "实体管理")
public class EntityController {


    @ApiOperation(value = "用户实体类")
    @GetMapping("/userEntity")
    @ResponseBody
    private User showUserEntity(@ApiParam(value = "用户实体类")User user){
        return user;
    }

    @ApiOperation(value = "实验实体类")
    @GetMapping("/expEntity")
    @ResponseBody
    private Exp showExpEntity(@ApiParam(value = "用户实体类") Exp exp){
        return exp;
    }

    @ApiOperation(value = "共享实体类")
    @GetMapping("/shareEntity")
    @ResponseBody
    private Share showExpEntity(@ApiParam(value = "共享实体类") Share share){
        return share;
    }

    @ApiOperation(value = "实验报告实体")
    @GetMapping("/recordEntity")
    @ResponseBody
    private Record recordEntity(@ApiParam(value = "实验报告实体") Record record){
        return record;
    }

    @ApiOperation(value = "历史代码实体")
    @GetMapping("/codeEntity")
    @ResponseBody
    private History historyEntity(@ApiParam(value = "历史代码实体") History history){
        return history;
    }

    @ApiOperation(value = "问答实体类")
    @GetMapping("/qtoaEntity")
    @ResponseBody
    private Qtoa qtoaEntity(@ApiParam(value = "问答实体类") Qtoa qtoa){
        return qtoa;
    }

    @ApiOperation(value = "班级实体类")
    @GetMapping("/gradeEntity")
    @ResponseBody
    private Tgrade gradeEntity(@ApiParam(value = "班级实体类") Tgrade tgrade){
        return tgrade;
    }


}
