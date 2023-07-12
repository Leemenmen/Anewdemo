package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.History;
import com.newdmsp.demo.entity.Share;
import com.newdmsp.demo.service.ShareService;
import com.newdmsp.demo.utils.Config;
import com.newdmsp.demo.utils.PageModel;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
//import net.sf.json.JSONArray;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.newdmsp.demo.utils.Config.ROOT_PATH;

@Slf4j
@Controller
@Api(tags = "共享模块")
public class ShareController {

    @Resource
    ShareService shareService;

    @ApiOperation(value = "学生查看共享数据", notes = "学生查看教师审核通过的共享文件 1. 默认显示所有数据；2.若是想要找到指定在uploadDes中赋值即可")
    //用在指定接口上，说明返回值
    @GetMapping("/shares/{flag}/{pageNo}/{mPageSize}")
    @ResponseBody
    public Result getShares(@ApiParam(value = "0是代码，1是数据", required = true) @PathVariable Integer flag,
                            @ApiParam(value = "上传描述", defaultValue = "null") @RequestParam String uploadDes,
                            @ApiParam(value = "页码", type = "path", required = true) @PathVariable int pageNo,
                            @ApiParam(value = "每页显示几条数据", type = "path", required = true) @PathVariable int mPageSize) {

        Share share = new Share();
        share.setFlag(flag);
        share.setUploadDes(uploadDes);
        PageModel model = new PageModel<>(pageNo, share);
        model.setPageSize(mPageSize);
        try {
            List<Share> tss = shareService.getShareByFlagPage(model);
            if (tss.size() >= 0) {
                Result<History> result = ResultUtil.success(tss);
                result.setTotal(shareService.getShareByFlagPageTotals(model));
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


    @ApiOperation(value = "查看自己提交的共享记录", notes = "")
    //用在指定接口上，说明返回值
    @GetMapping("/getMyShares/{pageNo}/{mPageSize}/{id}")
    @ResponseBody
    public Result getMyShares(@ApiParam(value = "学生id", required = true) @PathVariable Integer id,
//                            @ApiParam(value = "上传描述", defaultValue = "null")@RequestParam String uploadDes,
                                           @ApiParam(value = "页码", type = "path", required = true) @PathVariable int pageNo,
                                           @ApiParam(value = "每页显示几条数据", type = "path", required = true) @PathVariable int mPageSize) {

        Share share = new Share();
        share.setUserId(id);
//        share.setUploadDes(uploadDes);
        PageModel<Share> model = new PageModel<>(pageNo, share);
        model.setPageSize(mPageSize);
        Result result = new Result();
        try {
            List<Share> tss = shareService.getShareBystuId(model);

            result.setData(tss);
            result.setTotal(shareService.getShareBystuIdTotals(model));
            result.setCode(0);
            return result;

        } catch (Exception e) {
//            log.info(String.valueOf(e));

            return ResultUtil.unSuccess();
        }
    }

    /**
     * @param share
     * @param file
     * @return
     */
    @ApiOperation(value = "新增共享", notes = "1. 读取传入值；2. 生成文件路径名；3. 若路径不存在则新建； 4. 保存文件到指定路径； 5.将该条记录保存在数据库中")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "flag", value = "0代码；1数据", required = true),
            @ApiImplicitParam(type = "query", name = "userId", value = "上传者id", required = true),
            @ApiImplicitParam(type = "query", name = "uploadDes", value = "上传文件描述", required = true),
            // 这些值若是不定义，接口传入报错，值不会真正存入到数据库中
            @ApiImplicitParam(type = "query", name = "downloadCount", value = "下载次数", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "gradeid", value = "班级名，不会存入数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "id", value = "不会影响数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "teachScore", value = "不会存入数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "tid", value = "不会影响数据库", required = false, defaultValue = "0")

    })
    @RequestMapping(value = "/uploadSource", method = RequestMethod.POST)
    @ResponseBody
    public Result uploadSource(Share share,
                               @ApiParam(value = "上传文件", required = true) MultipartFile file) {

//        log.info(String.valueOf(share));
        // 1. 读取传入值；2. 生成文件路径名；
        String fileName = new SimpleDateFormat("yyyy_MM_dd").format(new Date()) + '/' + share.getUserId() + "_" + new SimpleDateFormat("HHmmss").format(new Date()) + "_" + file.getOriginalFilename();
        String pathString = null;
        String baseDir = ROOT_PATH + "/shareFile/";
        String signal;
        if (!new File(baseDir).exists()) {
            new File(baseDir).mkdirs();
        }
        if (share.getFlag() == 0) {
            signal = "code/";

        } else {
            signal = "data/";
        }
        // 3. 若路径不存在则新建；
        if (!new File(baseDir + signal).exists()) {
            new File(baseDir + signal).mkdirs();
        }
        pathString = baseDir + signal + fileName;

        try {
            File files = new File(pathString);

            share.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            share.setStoragePath(fileName);
            if (!files.getParentFile().exists()) {
                files.getParentFile().mkdirs();
            }
            // 4. 保存文件到指定路径； 5.将该条记录保存在数据库中
            file.transferTo(files);
            shareService.addShare(share);

            return ResultUtil.success();

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.unSuccess();
        }
    }


    @ApiOperation(value = "教师查询学生共享文件", notes = "教师查看其所教授班级所有学生上传的共享文件：1. 根据flag查看：0是代码，1是数据； 2. 教师id，由session中后台取")
    @GetMapping("/testShare/{pageNo}/{mPageSize}")
    @ResponseBody
    public Result testShares(@ApiParam(value = "0是代码，1是数据", required = true) @RequestParam Integer flag,
                             @ApiParam(value = "教师id", required = true) @RequestParam Integer id,
                             @ApiParam(value = "页码", type = "path", required = true) @PathVariable int pageNo,
                             @ApiParam(value = "每页显示几条数据", type = "path", required = true) @PathVariable int mPageSize) {

        Share share = new Share();
        share.setFlag(flag);
        share.setTid(id);
        try {
            PageModel model = new PageModel<>(pageNo, share);
            model.setPageSize(mPageSize);
            List<Share> tss = shareService.getTestShareByFlagPage(model);
            if (tss.size() >= 0) {
                Result<History> result = ResultUtil.success(tss);
                result.setTotal(shareService.getTestShareByFlagPageTotals(model));
                if (result.getTotal() == 0) {
                    result.setMsg("没有查到相关数据");
                } else {
                    result.setMsg("数据获取成功");
                }
                return result;
            } else {
                return ResultUtil.unSuccess("查找失败！");
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }


    }


    @ApiOperation(value = "审批学生共享文件", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "审批标准：1通过，0不通过; ", name = "tid", required = true),
            @ApiImplicitParam(value = "评分", name = "teachScore", type = "query", required = true),
            @ApiImplicitParam(value = "共享id", name = "id", type = "query", required = true),

            // 这些值若是不定义，接口传入报错，值不会真正存入到数据库中
            @ApiImplicitParam(type = "query", name = "downloadCount", value = "下载次数", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "gradeid", value = "不会存入数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "userId", value = "不会影响数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "flag", value = "不会存入数据库", required = false, defaultValue = "0"),


    })
    @PostMapping("/checkShare")
    @ResponseBody
    public Result checkShare(@ApiParam(value = "实体类") Share share) {

        try {
            int num = shareService.upCheckShare(share);
            if (num > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }


}
