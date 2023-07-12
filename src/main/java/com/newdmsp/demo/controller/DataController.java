package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.DataSet;
import com.newdmsp.demo.service.DataService;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@Api(tags = "数据集")
public class DataController {

    @Resource
    DataService dataService;


    @ApiOperation(value = "获取所有", notes = "")
    @GetMapping("/dataSets/{sid}")
    @ResponseBody
    public Result getAllData(@ApiParam(value = "学生id", required = true) @PathVariable Integer sid) {

        try {
            List<DataSet> dataSets = dataService.getDefaultData(sid);

            return ResultUtil.success(dataSets);

        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }

    @ApiOperation(value = "自主数据集上传", notes = "1.设置sid、expId(0)、文件路径(文件名);2. 构建文件路径、文件存储; 3. 数据库记录")
    @PostMapping("/selfDataset/{sid}/{expId}")
    @ResponseBody
    public Result uploadSelfData(
            @ApiParam(value = "数据集文件", type = "body", required = true) @RequestParam MultipartFile file,
            @ApiParam(value = "学生id", required = true) @PathVariable Integer sid,
            @ApiParam(value = "学生id", required = true) @PathVariable Integer expId) {
        try {
            DataSet dataSet = new DataSet();
            String fileName = file.getOriginalFilename();
            dataSet.setSid(sid);
            dataSet.setExpId(expId);
            dataSet.setCreatime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String Path = System.getProperty("user.dir") + "/selfDataset/";
            if (!new File(Path).exists()) {
                new File(Path).mkdirs();
            }
            if (!new File(Path + sid + "/").exists()) {
                new File(Path + sid + "/").mkdirs();
            }

            File newFile = new File(Path + sid + "/", file.getOriginalFilename());

            if (newFile.exists()){
                //获取文件的前缀和后缀
                int index = fileName.lastIndexOf(".");
                String toPrefix = "";
                String toSuffix = "";
                if (index == -1) {
                    toPrefix = fileName;
                } else {
                    toPrefix = fileName.substring(0, index);
                    toSuffix = fileName.substring(index, fileName.length());
                }

                //重命名
                File directory = newFile.getParentFile();
                int i;
                for (i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
                    newFile = new File(directory, toPrefix + '(' + i + ')' + toSuffix);
                }
                log.info(String.valueOf(newFile));
            }
            dataSet.setDataPath(newFile.getName());
            file.transferTo(newFile);

            int flag = dataService.uploadSelfData(dataSet);
            if (flag > 0) {
                return ResultUtil.success(newFile.getName());
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (IOException e) {
//            e.printStackTrace();
            return ResultUtil.unSuccess(String.valueOf(e));
        }
    }


    /**
     * @param from 解析文件
     * @return
     */
    private String[] getFileInfo(File from) {
        String fileName = from.getName();
        int index = fileName.lastIndexOf(".");
        String toPrefix = "";
        String toSuffix = "";
        if (index == -1) {
            toPrefix = fileName;
        } else {
            toPrefix = fileName.substring(0, index);
            toSuffix = fileName.substring(index, fileName.length());
        }
        return new String[]{toPrefix, toSuffix};
    }


    /**
     * @param from：路径+文件名
     * @param toPrefix:文件名不包含后缀 test
     * @param toSuffix：文件名后缀    .xlsx
     * @return
     */

    private File createOrRenameFile(File from, String toPrefix, String toSuffix) {
        File directory = from.getParentFile();
        log.info(String.valueOf(from));
        if (!directory.exists()) {
            if (directory.mkdir()) {
                log.info("Created directory " + directory.getAbsolutePath());
            }
        }
        File newFile = new File(directory, toPrefix + toSuffix);
        int i;
        for (i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
            newFile = new File(directory, toPrefix + '(' + i + ')' + toSuffix);
        }
        if (from == newFile) {
            log.info("Couldn't rename file to " + newFile.getAbsolutePath());
            return from;
        }
        log.info(String.valueOf(newFile));
        return newFile;
    }
//
//    @ApiOperation(value = "自主数据集上传", notes = "")
//    @PostMapping("/expDataset/{tid}/{expId}")
//    @ResponseBody
//    public Result uploadExpData(
//            @ApiParam(value = "数据集文件", type = "body", required = true) @RequestParam MultipartFile file,
//            @ApiParam(value = "教师id", required = true) @PathVariable Integer tid,
//            @ApiParam(value = "实验id", required = true) @PathVariable Integer expId) {
//        try {
//            DataSet dataSet = new DataSet();
//            dataSet.setDataPath(file.getOriginalFilename());
//            dataSet.setTid(tid);
//            dataSet.setExpId(expId);
//            dataSet.setCreatime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//            String Path = System.getProperty("user.dir") + "/dataSet/";
//            if (!new File(Path).exists()) {
//                new File(Path).mkdirs();
//            }
//
//            File newFile = new File(Path, file.getOriginalFilename());
//            file.transferTo(newFile);
//            int flag = dataService.uploadSelfData(dataSet);
//            if (flag > 0) {
//                return ResultUtil.success();
//            } else {
//                return ResultUtil.unSuccess();
//            }
//        } catch (IOException e) {
////            e.printStackTrace();
//            return ResultUtil.unSuccess(String.valueOf(e));
//        }
//    }


    @ApiOperation(value = "查找实验对应数据集", notes = "")
    @GetMapping("/dataSetPathByExpId/{expId}")
    @ResponseBody
    public Result dataSetPathByExpId(@ApiParam(value = "实验id", required = true) @PathVariable Integer expId) {

        List<DataSet> dataSets = dataService.dataSetPathByExpId(expId);
        if (dataSets.size() > 0) {
            return ResultUtil.success(dataSets);
        } else {
            return ResultUtil.unSuccess();
        }
    }



}

