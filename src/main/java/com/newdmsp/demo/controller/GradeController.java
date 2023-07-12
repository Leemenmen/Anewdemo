package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.Grade;
import com.newdmsp.demo.entity.Tgrade;
import com.newdmsp.demo.service.GradeService;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@Slf4j
@Api(tags = "班级管理")

public class GradeController {


    @Resource
    GradeService gradeService;


    @ApiOperation(value = "新增班级", notes = "")
    @ResponseBody
    @PostMapping("/addGrade")
    public Result addGrade(@ApiParam(value = "班级名称", required = true) String gradeName, @ApiParam(value="教师id", required = true) Integer tid, @ApiParam(value = "授权的实验id串", required = true) String grantExp) {
        String[] expId = grantExp.split(",");
        int num = 0;
        Grade grade = new Grade();
        grade.setGname(gradeName);
        grade.setTid(tid);
        try {
            if (gradeService.addGrade(grade)>0) {
                for (String exp : expId) {
                    int flag = gradeService.addGradeExp(Integer.valueOf(exp), grade.getId());
                    if (flag > 0) {
                        num += 1;
                    }
                }
            } ;
            if (num == expId.length) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess(String.valueOf(e));
        }

    }

}
