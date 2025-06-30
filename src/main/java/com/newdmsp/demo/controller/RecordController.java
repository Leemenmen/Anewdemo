package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.*;
import com.newdmsp.demo.entity.Record;
import com.newdmsp.demo.service.ExpService;
import com.newdmsp.demo.service.RecordService;
import com.newdmsp.demo.service.ScoreService;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.newdmsp.demo.utils.Config.ROOT_PATH;

@Slf4j
@Controller
@Api(tags = "实验报告管理")
public class RecordController {

    @Resource
    RecordService recordService;
    @Resource
    ExpService expService;
    @Resource
    ScoreService scoreService;

    /**
     * 填写实验报告
     *
     * @return
     */
    @PostMapping("/addRecord1")
    @ResponseBody
    public Object addRecord(Record book) {
//        log.info("===file=="+file);
        HashMap<String, Object> map = new HashMap<>();
        // 存储图书信息到数据库
        Integer flag = null;
        try {
            flag = recordService.addRecord(book);
            if (flag != 0) {
                map.put("msg", "ok");
                map.put("code", 0);
            } else {
                map.put("msg", "error");
                map.put("code", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 防止id重复
            map.put("msg", "error");
            map.put("code", 1);
        }

        return map;
    }

    /**
     * 保存上传的代码
     */
    @PostMapping("/upload")
    @ResponseBody
    public Object upload(@RequestParam("expname") String expname,
                         @RequestParam("gname") String gname,
                         MultipartFile file,
//                         String fileName,
                         HttpSession session) {
        HashMap<String, Object> map = new HashMap<>();
        User loginUser = (User) session.getAttribute("loginUser");
        // 上传图片
        // 存储地址
        String realPath = "D:\\Summary\\test";
        String father = realPath + "\\" + expname;
        String son = father + "\\" + gname;

        File fatherFile = new File(father);
        File sonFile = new File(son);
        if (!fatherFile.exists()) {
            fatherFile.mkdirs();
            sonFile.mkdirs();
        } else {
            if (!sonFile.exists()) {
                sonFile.mkdirs();
            }
        }
        // 获取文件后缀
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        // 文件名
        String newFileName = loginUser.getUsername() + "." + extension;
        //处理文件上传
        try {
            file.transferTo(new File(son, newFileName));
        } catch (IOException e) {
            e.printStackTrace();
            map.put("msg", "error");
            map.put("code", 1);
        }
        map.put("msg", "ok");
        map.put("code", 0);
        return map;
    }


    @ApiOperation(value = "新增共享", notes = "1. 读取传入值；2. 生成文件路径名；3. 若路径不存在则新建； 4. 保存文件到指定路径； 5.将该条记录保存在数据库中")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "expid", required = true),
            @ApiImplicitParam(type = "query", name = "expprocess", required = true),
            @ApiImplicitParam(type = "query", name = "expresult", required = true),
            @ApiImplicitParam(type = "query", name = "expname", required = true),

            // 这些值若是不定义，接口传入报错，值不会真正存入到数据库中
            @ApiImplicitParam(type = "query", name = "count", value = "下载次数", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "gid", value = "班级名，不会存入数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "id", value = "不会影响数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "score", value = "不会存入数据库", required = false, defaultValue = "0"),
            @ApiImplicitParam(type = "query", name = "sid", value = "不会影响数据库", required = false, defaultValue = "0")
    })
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST)
    @ResponseBody
    public Result addRecord(Record record,
                            @ApiParam(value = "上传文件", required = true) MultipartFile file, HttpSession session) {

        try {


            User user = (User) session.getAttribute("loginUser");
            String fileName = user.getUsername() + ".py";
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            record.setSid(user.getId());
            record.setGid(user.getGradeid());
            record.setCreateTime(time);
            record.setLastTime(time);

            if (file != null) {

                String pathString = null;

                String baseDir = ROOT_PATH + "/stuRecord";
                if (!new File(baseDir).exists()) {
                    new File(baseDir).mkdirs();
                }

                pathString = baseDir + '/' + user.getGname() + '_' + record.getExpname();
                if (!new File(pathString).exists()) {
                    new File(pathString).mkdirs();
                }
                file.transferTo(new File(pathString, fileName));
                record.setRecordcode(fileName);

            }
            int num = 0;

            if (record.getId() > 0) {
                num = recordService.updateStuRecord(record);
            } else {
                num = recordService.addRecord(record);
            }
            if (num > 0) {
                return ResultUtil.success(record.getId());
            } else {
                return ResultUtil.unSuccess();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.unSuccess();
        }
    }


    @RequestMapping(value = "/addUploadTotalRecord", method = RequestMethod.POST)
    @ResponseBody
    @ExceptionHandler
    public Result addUploadRecord(Record record,
                                  @ApiParam(value = "上传文件", required = true) MultipartFile[] files, MultipartFile codeFile, MultipartFile[] file,String deleteReName, HttpSession session, HttpServletRequest request) {
        try {

            // 数据修改成功的标志
            int num = 0;
            int png = 0;
            int csv = 0;

            // 获取用户基本信息
            User user = (User) session.getAttribute("loginUser");
            String fileName = user.getUsername() + ".py";


            // 数据库时间字段
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // 最后一次修改时间
            record.setLastTime(time);
            record.setRecordcode(fileName);

            // 设置文件存储目录，不存在则创建
            String pathString = null;
            String baseDir = ROOT_PATH + "/stuRecord";
            if (!new File(baseDir).exists()) {
                new File(baseDir).mkdirs();
            }
            pathString = baseDir + '/' + user.getGname() + '_' + record.getExpname();
            if (!new File(pathString).exists()) {
                new File(pathString).mkdirs();
            }


            //代码文件

            if (!codeFile.isEmpty()){
                codeFile.transferTo(new File(pathString, fileName));

            }

//            if (file != null) {
//
//
//                for (MultipartFile pyFile : file) {
//                    String extension = pyFile.getOriginalFilename().substring(pyFile.getOriginalFilename().lastIndexOf(".") + 1);
//                    if (extension.equals("py")) {
//                        pyFile.transferTo(new File(pathString, fileName));
//                    }
//                }
//
//            }

            if (record.getId() > 0) {   // 保存修改

                String csvDel = ""; // 记录删除的csv文件索引
                String pngDel = ""; // 记录删除的png文件索引

                // 是否需要删除附件
                if (!deleteReName.isEmpty()) {
                    char[] deleteReNameChar = deleteReName.toCharArray();

                    for (int c = 0; c <= deleteReNameChar.length / 5; c++) {
                        if (deleteReNameChar[c * 5] == 'c') {
                            if (csvDel.length() == 0) {
                                csvDel += deleteReNameChar[c * 5 + 3];
                            } else {
                                csvDel += ',';
                                csvDel += deleteReNameChar[c * 5 + 3];
                            }
                        }
                        if (deleteReNameChar[c * 5] == 'p') {
                            if (pngDel.length() == 0) {
                                pngDel += deleteReNameChar[c * 5 + 3];
                            } else {
                                pngDel += ',';
                                pngDel += deleteReNameChar[c * 5 + 3];
                            }
                        }
                    }

                    // 组成已删除文件后缀
                    String[] csvDelArr = csvDel.split(",");
                    String[] pngDelArr = pngDel.split(",");

                    // 有删除的csv文件则执行
                    if (csvDelArr[0].length() > 0) {
                        // 组成未删除文件后缀
                        String[] exsitCsvArr = obtainExitsNumber(record.getCsv(), csvDelArr);
                        // 删除文件
                        for (int c = 0; c < csvDelArr.length; c++) {
                            File csvFile = new File(pathString, user.getUsername() + "_" + csvDelArr[c] + ".csv");
                            csvFile.renameTo(new File(pathString, user.getUsername() + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".csv"));

                        }
                        // 重命名csv文件
                        for (int oc = 0; oc < exsitCsvArr.length; oc++) {
                            File renameCsvFile = new File(pathString, user.getUsername() + "_" + exsitCsvArr[oc] + ".csv");
                            File newNameCsvFile = new File(pathString, user.getUsername() + "_" + oc + ".csv");
                            renameCsvFile.renameTo(newNameCsvFile);
                        }
                    }
                    // 有删除的png文件则执行
                    if (pngDelArr[0].length() > 0) {

                        // 组成未删除文件后缀
                        String[] exsitPngArr = obtainExitsNumber(record.getPng(), pngDelArr);
                        // 删除文件
                        for (int p = 0; p < pngDelArr.length; p++) {
                            File pngFile = new File(pathString, user.getUsername() + "_" + pngDelArr[p] + ".png");
                            pngFile.renameTo(new File(pathString, user.getUsername() + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".png"));
                        }
                        // 重命名png文件
                        for (int op = 0; op < exsitPngArr.length; op++) {
                            File renamePngFile = new File(pathString, user.getUsername() + "_" + exsitPngArr[op] + ".png");
                            File newNamePngFile = new File(pathString, user.getUsername() + "_" + op + ".png");
                            renamePngFile.renameTo(newNamePngFile);
                        }
                    }

                    // 上传结果附件
//                    int k = 0;
                    if(files != null){
                        if (files.length > 0) {
                            for (MultipartFile reFile : files) {
                                String reFileName = reFile.getOriginalFilename();
                                String fileType = FilenameUtils.getExtension(reFileName).toLowerCase();
                                if (fileType.equals("png")) {
                                    if (pngDelArr[0].length() == 0) {
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getPng() + png) + "." + fileType));
                                    } else {
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getPng() - pngDelArr.length + png) + "." + fileType));
                                    }
                                    png++;
                                } else {
                                    if (csvDelArr[0].length() == 0) {
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getCsv() + csv) + "." + fileType));
                                    } else {
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getCsv() - csvDelArr.length + csv) + "." + fileType));
                                    }
                                    csv++;
                                }
//                            k = k+1;
                            }
                        }

                    }
                    // 设置附件数量
                    if (csvDelArr[0].length() > 0) {
                        record.setCsv(record.getCsv() - csvDelArr.length + csv);
                    } else {
                        record.setCsv(record.getCsv() + csv);
                    }
                    if (pngDelArr[0].length() > 0) {
                        record.setPng(record.getPng() - pngDelArr.length + png);
                    } else {
                        record.setPng(record.getPng() + png);
                    }

                } else {
                    // 上传结果附件
                    if (files != null){
                        if (files.length > 0) {
                            for (MultipartFile reFile : files) {
                                String reFileName = reFile.getOriginalFilename();
                                String fileType = FilenameUtils.getExtension(reFileName).toLowerCase();
                                if (fileType.equals("png")) {
                                    if (record.getPng()==0){
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + png + "." + fileType));
                                    }else {
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getPng() + png ) + "." + fileType));
                                    }

                                    png++;
                                } else {

                                    if (record.getCsv()==0){
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" +  csv  + "." + fileType));

                                    }else {
                                        reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getCsv() + csv) + "." + fileType));

                                    }
                                    csv++;
                                }
                            }
                        }
                    }

                    // 设置附件数量
                    record.setCsv(record.getCsv() + csv);
                    record.setPng(record.getPng() + png);
                }

                num = recordService.updateStuRecord(record);

            } else {                     // 初次保存


                // 上传结果附件
                if (files.length > 0) {
                    for (MultipartFile reFile : files) {
                        String reFileName = reFile.getOriginalFilename();
                        String fileType = FilenameUtils.getExtension(reFileName).toLowerCase();
                        if (fileType.equals("png")) {
                            reFile.transferTo(new File(pathString, user.getUsername() + "_" + png + "." + fileType));
                            png++;
                        } else {
                            reFile.transferTo(new File(pathString, user.getUsername() + "_" + csv + "." + fileType));
                            csv++;
                        }

                    }
                }

                record.setSid(user.getId());
                record.setGid(user.getGradeid());
                record.setCreateTime(time);
                record.setPng(png);
                record.setCsv(csv);

                num = recordService.addRecord(record);
            }
            if (num > 0) {
                return ResultUtil.success(record.getId());
            } else {
                return ResultUtil.unSuccess();
            }


        } catch (Exception e) {
            e.printStackTrace();
//            log.info(String.valueOf(e));
            return ResultUtil.unSuccess(String.valueOf(e));
        }
    }


    @RequestMapping(value = "/updateStepRecord", method = RequestMethod.POST)
    @ResponseBody
    @ExceptionHandler
    public Result updateStepRecord(Record record,
                                   @ApiParam(value = "上传文件", required = true) MultipartFile[] files, MultipartFile[] file, String deleteReName, HttpSession session, HttpServletRequest request) {
        try {

            // 数据修改成功的标志
            int num = 0;
            int png = 0;
            int csv = 0;

            // 获取用户基本信息
            User user = (User) session.getAttribute("loginUser");
            String fileName = user.getUsername() + ".py";


            // 数据库时间字段
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            // 最后一次修改时间
            record.setLastTime(time);
            record.setRecordcode(fileName);

            // 设置文件存储目录，不存在则创建
            String pathString = null;
            String baseDir = ROOT_PATH + "/stuRecord";
            if (!new File(baseDir).exists()) {
                new File(baseDir).mkdirs();
            }
            pathString = baseDir + '/' + user.getGname() + '_' + record.getExpname();
            if (!new File(pathString).exists()) {
                new File(pathString).mkdirs();
            }


            //代码文件
            if (file != null) {
                for (MultipartFile pyFile : file) {
                    String extension = pyFile.getOriginalFilename().substring(pyFile.getOriginalFilename().lastIndexOf(".") + 1);
                    if (extension.equals("py")) {
                        pyFile.transferTo(new File(pathString, fileName));
                    }
                }


            }

            // 保存修改

            String csvDel = ""; // 记录删除的csv文件索引
            String pngDel = ""; // 记录删除的png文件索引

            // 是否需要删除附件
            if (!deleteReName.isEmpty()) {
                char[] deleteReNameChar = deleteReName.toCharArray();

                for (int c = 0; c <= deleteReNameChar.length / 5; c++) {
                    if (deleteReNameChar[c * 5] == 'c') {
                        if (csvDel.length() == 0) {
                            csvDel += deleteReNameChar[c * 5 + 3];
                        } else {
                            csvDel += ',';
                            csvDel += deleteReNameChar[c * 5 + 3];
                        }
                    }
                    if (deleteReNameChar[c * 5] == 'p') {
                        if (pngDel.length() == 0) {
                            pngDel += deleteReNameChar[c * 5 + 3];
                        } else {
                            pngDel += ',';
                            pngDel += deleteReNameChar[c * 5 + 3];
                        }
                    }
                }

                // 组成已删除文件后缀
                String[] csvDelArr = csvDel.split(",");
                String[] pngDelArr = pngDel.split(",");

                // 有删除的csv文件则执行
                if (csvDelArr[0].length() > 0) {
                    // 组成未删除文件后缀
                    String[] exsitCsvArr = obtainExitsNumber(record.getCsv(), csvDelArr);
                    // 删除文件
                    for (int c = 0; c < csvDelArr.length; c++) {
                        File csvFile = new File(pathString, user.getUsername() + "_" + csvDelArr[c] + ".csv");
                        csvFile.renameTo(new File(pathString, user.getUsername() + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".csv"));

                    }
                    // 重命名csv文件
                    for (int oc = 0; oc < exsitCsvArr.length; oc++) {
                        File renameCsvFile = new File(pathString, user.getUsername() + "_" + exsitCsvArr[oc] + ".csv");
                        File newNameCsvFile = new File(pathString, user.getUsername() + "_" + oc + ".csv");
                        renameCsvFile.renameTo(newNameCsvFile);
                    }
                }
                // 有删除的png文件则执行
                if (pngDelArr[0].length() > 0) {

                    // 组成未删除文件后缀
                    String[] exsitPngArr = obtainExitsNumber(record.getPng(), pngDelArr);
                    // 删除文件
                    for (int p = 0; p < pngDelArr.length; p++) {
                        File pngFile = new File(pathString, user.getUsername() + "_" + pngDelArr[p] + ".png");
                        pngFile.renameTo(new File(pathString, user.getUsername() + "_" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + ".png"));
                    }
                    // 重命名png文件
                    for (int op = 0; op < exsitPngArr.length; op++) {
                        File renamePngFile = new File(pathString, user.getUsername() + "_" + exsitPngArr[op] + ".png");
                        File newNamePngFile = new File(pathString, user.getUsername() + "_" + op + ".png");
                        renamePngFile.renameTo(newNamePngFile);
                    }
                }

                // 上传结果附件
//                    int k = 0;
                if (files.length > 0) {
                    for (MultipartFile reFile : files) {
                        String reFileName = reFile.getOriginalFilename();
                        String fileType = FilenameUtils.getExtension(reFileName).toLowerCase();
                        if (fileType.equals("png")) {
                            if (pngDelArr[0].length() == 0) {
                                reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getPng() + png) + "." + fileType));
                            } else {
                                reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getPng() - pngDelArr.length + png) + "." + fileType));
                            }
                            png++;
                        } else {
                            if (csvDelArr[0].length() == 0) {
                                reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getCsv() + csv) + "." + fileType));
                            } else {
                                reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getCsv() - csvDelArr.length + csv) + "." + fileType));
                            }
                            csv++;
                        }
//                            k = k+1;
                    }
                }
                // 设置附件数量
                if (csvDelArr[0].length() > 0) {
                    record.setCsv(record.getCsv() - csvDelArr.length + csv);
                } else {
                    record.setCsv(record.getCsv() + csv);
                }
                if (pngDelArr[0].length() > 0) {
                    record.setPng(record.getPng() - pngDelArr.length + png);
                } else {
                    record.setPng(record.getPng() + png);
                }

            } else {
                // 上传结果附件
                int j = 0;
                if (files.length > 0) {
                    for (MultipartFile reFile : files) {
                        String reFileName = reFile.getOriginalFilename();
                        String fileType = FilenameUtils.getExtension(reFileName).toLowerCase();
                        if (fileType.equals("png")) {
                            reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getPng() + j) + "." + fileType));
                            png++;
                        } else {
                            reFile.transferTo(new File(pathString, user.getUsername() + "_" + (record.getCsv() + j) + "." + fileType));
                            csv++;
                        }
                        j = j + 1;
                    }
                }
                // 设置附件数量
                record.setCsv(record.getCsv() + csv);
                record.setPng(record.getPng() + png);
            }

            num = recordService.updateStepRecord(record);


            if (num > 0) {
                return ResultUtil.success(record.getId());
            } else {
                return ResultUtil.unSuccess();
            }


        } catch (Exception e) {
            e.printStackTrace();
//            log.info(String.valueOf(e));
            return ResultUtil.unSuccess(String.valueOf(e));
        }
    }

    public String[] obtainExitsNumber(Integer total, String[] less) { //(数字，部分数字数组)

        String[] totalNumber = new String[total];
        for (int tc = 0; tc < total; tc++) {
            totalNumber[tc] = String.valueOf(tc);
        }
        Set<String> totalSet = new HashSet<String>(Arrays.asList(totalNumber));

        for (String p : less) {
            // 如果集合里有相同的就删掉，如果没有就将值添加到集合
            if (totalSet.contains(p)) {
                totalSet.remove(p);
            } else {
                totalSet.add(p);
            }
        }
        return totalSet.toArray(new String[total - less.length]);
    }

    @RequestMapping(value = "/addRecordTest", method = RequestMethod.POST)
    @ResponseBody
    public Result addRecordTest(Record record,
                                @ApiParam(value = "上传文件", required = true) MultipartFile file, HttpSession session) {


        log.info(String.valueOf(record));
        log.info(file.getOriginalFilename());
        return ResultUtil.success();
    }


    @ApiOperation(value = "教师实验报告打分", notes = "1. 教师对已经提交了实验报告的学生进行评分")
    @ResponseBody
    @PostMapping("/upRecord")
    public Object upRecord(@ApiParam(value = "分数", name = "score", required = true, type = "query") String score,
                           @ApiParam(value = "报告id", name = "id", required = true, type = "query") String id) {
        try {
            int num = recordService.upRecord(id, score);
//            log.info("num=" + num);
            if (num > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }


    @ApiOperation(value = "教师实验报告打分", notes = "1. 教师对已经提交了实验报告的学生进行评分")
    @ResponseBody
    @PostMapping("/upStepRecord")
    public Object upStepRecord(Score score) {
        try {
            int num;
            String totalScore;
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            if (score.getDataDescScore() == null){
                score.setDataDescScore(0.0);
                totalScore = String.valueOf(score.getVideoDescScore()*(0.1)+(score.getExperienceScore()+score.getExpProcessScore()+score.getExpResultScore())*(0.1)+(score.getRecordCodeScore()+score.getTotalDescScore())*(0.3));
            }else {
                totalScore = String.valueOf((score.getVideoDescScore()+score.getDataDescScore())*(0.05)+(score.getExperienceScore()+score.getExpProcessScore()+score.getExpResultScore())*(0.1)+(score.getRecordCodeScore()+score.getTotalDescScore())*(0.3));
            }
            if (score.getId() != null){
                // 修改分数
                score.setLastTime(time);
                num = scoreService.updateScore(score);
            }else {
                score.setCreateTime(time);
                num = scoreService.addScore(score);
            }

            recordService.upRecord(String.valueOf(score.getRecordId()), totalScore);
            if (num > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }

    @ApiOperation(value = "获取实验报告详细打分", notes = "")
    @ResponseBody
    @GetMapping("/detailScore/{recordId}")
    public Object detailScore(@ApiParam(value = "实验报告id", required = true) @PathVariable String recordId) {
        try {

            Score score = scoreService.getDetailScore(recordId);
            return ResultUtil.success(score);
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }


    @ApiOperation(value = "学生查看自己的实验报告", notes = "1. 根据传入值查找实验报告")
    @ResponseBody
    @GetMapping("/findRecord/{expId}")
    public Object findRecord(@ApiParam(value = "实验id", required = true) @PathVariable String expId, HttpSession
            session) {
        try {
            User user = (User) session.getAttribute("loginUser");
            Record record1 = recordService.fingRecord(Integer.valueOf(expId), user.getId());
            return ResultUtil.success(record1);
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }

    @ApiOperation(value = "提交实验报告", notes = "根据实验报告id查找记录，修改flag")
    @ResponseBody
    @PostMapping("/submitRecord/{recordId}")
    public Object submitRecord(@ApiParam(value = "实验报告id", required = true) @PathVariable Integer recordId) {
        try {

            int record1 = recordService.submitRecord(recordId);
            if (record1 > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }

    @ApiOperation(value = "提交实验报告", notes = "根据实验报告id查找记录，修改flag")
    @ResponseBody
    @PostMapping("/submitRecord1/{recordId}")
    public Object submitRecord1(@ApiParam(value = "实验报告id", required = true) @PathVariable Integer recordId) {
        try {

            int record1 = recordService.submitRecordTime(recordId, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            if (record1 > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }


    @ApiOperation(value = "步骤汇总提交", notes = "根据实验报告id查找记录，修改flag=2")
    @ResponseBody
    @PostMapping("/submitStepRecord/{recordId}")
    public Object submitStepRecord(@ApiParam(value = "实验报告id", required = true) @PathVariable Integer recordId) {
        try {

            int record1 = recordService.submitStepRecord(recordId);
            if (record1 > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }


    @ApiOperation(value = "提交场景体会", notes = "")
    @ResponseBody
    @PostMapping("/addDesc")
//    public Result addVideoDesc(@RequestBody Record record) {
    public Result addVideoDesc(Record record) {


        try {
            Map<String, Object> upDesMap = new HashMap<String, Object>();
            if (record.getId() == 0) {
                int num = recordService.addDesc(record);
                if (num > 0) {
                    return ResultUtil.success(record.getId());
                } else {
                    return ResultUtil.unSuccess("异常");
                }
            } else {
                Class cls = record.getClass();
                Field[] fields = cls.getDeclaredFields();
                for (int i = 4; i < fields.length; i++) {
                    Field f = fields[i];
                    f.setAccessible(true);
                    if (f.get(record) != null) {
                        upDesMap.put(f.getName(), f.get(record));
                    }
                }
                if (record.getTotalDesc()!=null){
                    upDesMap.remove("createTime");
                    if (record.getCreateTime().isEmpty()) {
                        upDesMap.put("createTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    }

                }
                boolean res = recordService.dynamicUpTable(upDesMap, record.getId());
                if (res) {
                    return ResultUtil.success();
                } else {
                    return ResultUtil.unSuccess();
                }
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess(String.valueOf(e));
        }

    }

    @ApiOperation(value = "提交场景体会", notes = "")
    @ResponseBody
    @PostMapping("/addDesc1")
//    public Result addVideoDesc(@RequestBody Record record) {
    public Result addVideoDesc1(Record record, String dataDesc, String dataDescVal) {


        try {
//            Map<String, Object> upDesMap = new HashMap<String, Object>();
            if (record.getId() == 0) {
                int num = recordService.addDesc(record);
                if (num > 0) {
                    return ResultUtil.success(record.getId());
                } else {
                    return ResultUtil.unSuccess("异常");
                }
            } else {

                int res = recordService.addDescSet(dataDesc, dataDescVal,record.getId());
                if (res>0) {
                    return ResultUtil.success();
                } else {
                    return ResultUtil.unSuccess();
                }
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess(String.valueOf(e));
        }

    }

    @ApiOperation(value = "获取实验基本信息和实验报告情况")
    @ResponseBody
    @GetMapping("/getExpAndRecord/{expId}/{sid}")
    public Result getExpAndRecord(@PathVariable Integer expId, @PathVariable Integer sid) {

        try {
            Result result = new Result();
            Record record = recordService.getExpAndRecord(expId, sid);
            if (record == null) {
                Exp exp = expService.getExpsByExpId(expId);
                result.setData(exp);
                result.setCode(200);
                result.setTotal(0);
            } else {
                result.setData(record);
                result.setCode(200);
                result.setTotal(record.getId());
            }
            return result;
        } catch (Exception e) {
            return ResultUtil.unSuccess(String.valueOf(e));
        }

    }

}
