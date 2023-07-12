package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.Qtoa;
import com.newdmsp.demo.service.QtoaService;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@Api(tags = "问答模块")
public class QtoCtroller {

    @Resource
    QtoaService qtoaService;


    @ApiOperation(value = "查询问答记录", notes = "根据学生id查找所有提问和回答条目")
    @ResponseBody
    @GetMapping("/qToa")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学生id", name = "sid", type = "query", required = true)
    })
    public Result getQtoa(String sid) {
        try {
            Qtoa qtoa = new Qtoa();
            qtoa.setSid(Integer.valueOf(sid));
            List<Qtoa> qtoas = qtoaService.getQtoa(qtoa);
            return ResultUtil.success(qtoas);

        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }

    @ResponseBody
    @PostMapping("/question")
    @ApiOperation(value = "新增提问")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学生id", name = "sid", required = true, type = "query"),
            @ApiImplicitParam(value = "问题", name = "question", required = true, type = "query")
    })
    public Result add(String sid, String question) {

        try {
            Qtoa qtoa = new Qtoa();
            qtoa.setSid(Integer.valueOf(sid));
            qtoa.setQuestion(question);
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            qtoa.setQueTime(time);
            int flag = qtoaService.addQtoa(qtoa);
            if (flag>0) {
                return ResultUtil.success("提问成功");
            } else {
                return ResultUtil.unSuccess("失败");
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }


    }

    @ApiOperation(value = "教师回答问题", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "问题id", name = "id", type = "query", required = true),
            @ApiImplicitParam(value = "教师回答", name = "answer", type = "query", required = true)
    })
    @ResponseBody
    @PostMapping("/aQuestion")
    public Result update(String id, String answer) {
        try {
            Qtoa qtoa = new Qtoa();
            qtoa.setId(Integer.valueOf(id));
            qtoa.setAnswer(answer);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            qtoa.setAnsTime(date);
//            log.info(String.valueOf(qtoa));
            int num = qtoaService.update(qtoa);
            if (num > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }

    /**
     * 教师获取班级学生所提的问题
     *
     * @param qtoa
     * @return
     */
    @PostMapping("/getQtoaTeach")
    @ResponseBody
    public Result getQtoaTeach(Qtoa qtoa) {
//        log.info("==qtoa=="+qtoa);
        List<Qtoa> qtoas = qtoaService.getQtoaTeach(qtoa);
//        log.info("==qtoas=="+qtoas);
        return ResultUtil.success(qtoas);
    }

}
