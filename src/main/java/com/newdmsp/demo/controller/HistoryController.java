package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.History;
import com.newdmsp.demo.entity.User;
import com.newdmsp.demo.service.HistoryService;
import com.newdmsp.demo.utils.PageModel;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@Api(tags = "历史代码")
public class HistoryController {

    @Resource
    HistoryService historyService;

    @ApiOperation(value = "查看历史代码", notes = "1. 根据用户id遍历出所有提交运行的代码; 2. 用户id在session中取出")
    @ResponseBody
    @GetMapping("/getHistory/{pageNo}/{pageSize}")
    public Result getHistory(@ApiParam(value = "学生id", required = true) @RequestParam Integer id,
                             @ApiParam(value = "页码", type = "path", required = true) @PathVariable int pageNo,
                             @ApiParam(value = "每页显示几条数据", type = "path", required = true) @PathVariable int pageSize) {
        User user = new User();
        user.setId(id);
        PageModel model = new PageModel<>(pageNo, user);
        model.setPageSize(pageSize);
        try {
            List<History> tss = historyService.getHistoryByPage(model);
            if (tss.size() >= 0) {
                Result<History> result = ResultUtil.success(tss);
                result.setTotal(historyService.getHistoryByTotals(model));
                if (result.getTotal() == 0) {
                    result.setMsg("没有查到相关数据");
                } else {
                    result.setMsg("数据获取成功");
                }
                return result;
            } else {
                return ResultUtil.unSuccess("没有找到符合条件的属性！");
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }

    }


}
