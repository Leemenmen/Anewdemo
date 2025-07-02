package com.newdmsp.demo.controller;

import com.newdmsp.demo.entity.*;
import com.newdmsp.demo.service.DataService;
import com.newdmsp.demo.service.ExpService;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.newdmsp.demo.utils.Config.ROOT_PATH;

@Slf4j
@Controller
@Api(tags = "实验管理")
public class ExpController {

    //    static int i = 0;
    @Resource
    ExpService expService;
    @Resource
    DataService dataService;

    @ResponseBody
    @RequestMapping("/test")
    public String getTest() {
        return "test";
    }

    @ApiOperation(value = "模糊查询", notes = "根据实验名称查询")
    @ApiImplicitParams({
            @ApiImplicitParam(type = "query", name = "expname", value = "实验名称", required = true, defaultValue = "null")
    })
    @ResponseBody
    @PostMapping("/getExps")
    public Result getExps(@ApiParam(value = "实验实体") Exp exp) {
        try {
            List<Exp> exps = expService.getExps(exp);
            if (exps != null) {
                return ResultUtil.success(exps);
            } else {
                return ResultUtil.success();
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }

    @ApiOperation(value = "实验细节", notes = "指定实验id返回实验细节")
    @ResponseBody
    @PostMapping("/getExpsByExpId/{expId}")
    public Result getExpsByExpId(@ApiParam(value = "实验id", required = true) @PathVariable("expId") Integer expId) {
        try {
            Exp exp = expService.getExpsByExpId(expId);
            return ResultUtil.success(exp);
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }

    @ApiOperation(value = "实验名称", notes = "根据班级获取所有实验名称")
    @ResponseBody
    @PostMapping("/getExpsByGradeId/{gradeId}")
    public Result getExpsByGradeId(@ApiParam(value = "班级id") @PathVariable Integer gradeId) {
        try {
            List<Exp> exp = expService.getExpsByGradeId(gradeId);
            return ResultUtil.success(exp);
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }
    }

    @ApiOperation(value = "查询班级实验", notes = "获取班级和实验id、名称的映射")
    @ResponseBody
    @GetMapping("/gToe")
    public Result getGtoe() {
        try {
            List<Exp> exps = expService.getGtoe();
            return ResultUtil.success(exps);
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }

    @ApiOperation(value = "查询教师班级实验", notes = "")
    @ResponseBody
    @GetMapping("/getGradeExp/{id}")
    public Result getGradeExp(@ApiParam(value = "教师id", name = "id", required = true) @PathVariable String id) {
        try {
            List<Exp> exps = expService.getTeachGradeExp(Integer.valueOf(id));
            return ResultUtil.success(exps);
        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }

    }


    @ApiOperation(value = "新增实验", notes = "1. 设置上传路径; 2. 补充上传者id, 实验代码名称, 创建时间; 3. 文件上传、数据库新增记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "实验名称", name = "expname", type = "query", required = true),
            @ApiImplicitParam(value = "实验目的", name = "exptarget", type = "query", required = true),
            @ApiImplicitParam(value = "实验任务", name = "expwork", type = "query", required = true),
            @ApiImplicitParam(value = "实验代码名称", name = "expcodeurl", type = "query", required = true)
    })
    @PostMapping("/expUpload")
    @ResponseBody
    public Result expUpload(Exp exp,
                            @ApiParam(value = "上传文件") MultipartFile file, HttpSession session) throws IOException {


        // 1. 设置上传路径
        String baseDir = ROOT_PATH + "/dmspCode";
        if (!new File(baseDir).exists()) {
            new File(baseDir).mkdirs();
        }

        // 2. 补充上传者id, 实验代码名称, 创建时间
        String fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + exp.getExpcodeurl();
        User user = (User) session.getAttribute("loginUser");
        exp.setUpid(user.getId());
        exp.setExpcodeurl(fileName);
        exp.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        // 3. 文件上传、数据库新增记录
        File newfile = new File(baseDir, fileName);
        try {
            //将文件上传到指定路径，并以新的名字命名
            file.transferTo(newfile);
            //在数据表中添加相应的一行信息
            int num = expService.addExp(exp);
            if (num > 0) {
                return ResultUtil.success("上传成功");
            } else {
                return ResultUtil.unSuccess("上传失败");
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess("创建失败");
        }

    }


    // 测试同时上传 数据集和代码
    @ApiOperation(value = "新增实验、数据集", notes = "1. 设置上传路径; 2. 补充上传者id, 实验代码名称, 创建时间; 3. 依次文件上传、数据库新增记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "实验名称", name = "expname", type = "query", required = true),
            @ApiImplicitParam(value = "实验目的", name = "exptarget", type = "query", required = true),
            @ApiImplicitParam(value = "实验任务", name = "expwork", type = "query", required = true),
            @ApiImplicitParam(value = "上传者id", name = "upid", type = "query", required = true),
            @ApiImplicitParam(value = "数据集标志", name = "dataFlag", type = "query", required = true)
    })
    @ResponseBody
    @PostMapping("/textExpData")
    public Result upload(Exp exp,
                         @ApiParam(value = "数据集文件，可多个") @RequestParam("files") MultipartFile[] files,
                         @ApiParam(value = "代码文件，只能1个") MultipartFile file,
                         HttpServletRequest request) {
        try {
            log.info(String.valueOf(exp));

            // 1. 设置代码上传路径
            String baseDir = ROOT_PATH + "/dmspCode";
            if (!new File(baseDir).exists()) {
                new File(baseDir).mkdirs();
            }

            // 当前时间
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // 2. 补充上传者id, 实验代码名称, 创建时间
            String fileName = exp.getExpname() + "_" + exp.getUpid() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".py";
            exp.setExpcodeurl(fileName);
            exp.setCreateTime(nowDate);

            // 3. 文件上传、数据库新增记录
            File newfile = new File(baseDir, fileName);
            file.transferTo(newfile);
            //在数据表中添加相应的一行信息
            int num = expService.addExp(exp);
//            if (num > 0 && exp.getDataFlag().equals("1")) {
//                log.info(String.valueOf(exp));
//                return ResultUtil.success("代码上传成功");
//            } else {
//                return ResultUtil.unSuccess("代码上传失败");
//            }


            StringBuilder msg = new StringBuilder();

            msg = new StringBuilder("代码上传成功");
//            log.info(String.valueOf(exp));
            if (exp.getFlag() > 1) {
//                log.info("有数据集");
                for (MultipartFile datafile : files) { //循环遍历，取出单个文件，下面的操作和单个文件就一样了
                    if (datafile != null) { //这个判断必须要加
                        // 取得当前上传文件的文件名称
                        String dataBaseDir = ROOT_PATH + "/dmspCodeData";
                        if (!new File(dataBaseDir).exists()) {
                            new File(dataBaseDir).mkdirs();
                        }
                        String dataFileName = datafile.getOriginalFilename();
//                        log.info(dataFileName);
                        // 3. 文件上传、数据库新增记录
                        File dataFile = new File(dataBaseDir, dataFileName);
                        datafile.transferTo(dataFile);

                        DataSet dataSet = new DataSet();
                        dataSet.setExpId(exp.getId());
                        dataSet.setDataPath(dataFileName);
                        dataSet.setTid(exp.getUpid());
                        dataSet.setCreatime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                        //在数据表中添加相应的一行信息
                        int flag = dataService.uploadSelfData(dataSet);
                        if (flag > 0) {
                            msg.append("数据" + dataFileName + "上传成功");
                        } else {
                            msg.append("数据" + dataFileName + "上传失败");
                        }
                    }
                }
            }
            return ResultUtil.success(msg.toString());


        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.unSuccess("异常");
        }
    }


    // 测试同时上传 数据集和代码
    @ApiOperation(value = "新增实验、数据集", notes = "1. 设置上传路径; 2. 补充上传者id, 实验代码名称, 创建时间; 3. 依次文件上传、数据库新增记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "实验名称", name = "expname", type = "query", required = true),
            @ApiImplicitParam(value = "实验目的", name = "exptarget", type = "query", required = true),
            @ApiImplicitParam(value = "实验任务", name = "expwork", type = "query", required = true),
            @ApiImplicitParam(value = "上传者id", name = "upid", type = "query", required = true),
            @ApiImplicitParam(value = "数据集标志", name = "dataFlag", type = "query", required = true)
    })
    @ResponseBody
    @PostMapping("/textExpData1")
    public Result upload1(Exp exp,
                          @ApiParam(value = "数据集文件，可多个") MultipartFile dataSet,
                          @ApiParam(value = "代码文件，只能1个") MultipartFile file,
                          HttpServletRequest request) {
        try {
            log.info(String.valueOf(exp));

            MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
            MultipartFile code = multipartHttpServletRequest.getFile("file");

            if (exp.getFlag() > 0) {
                List<MultipartFile> data = multipartHttpServletRequest.getFiles("dataSet");
                for (MultipartFile datafile : data) {
                    if (datafile != null) {

                        String dataBaseDir = ROOT_PATH + "/dmspCodeData";
                        if (!new File(dataBaseDir).exists()) {
                            new File(dataBaseDir).mkdirs();
                        }
                        String dataFileName = datafile.getOriginalFilename();
                        log.info(dataFileName);
                        // 3. 文件上传、数据库新增记录
                        File dataFile = new File(dataBaseDir, dataFileName);
                        datafile.transferTo(dataFile);
                    }
                }
            }

            // 1. 设置代码上传路径
            String baseDir = ROOT_PATH + "/dmspCode";
            if (!new File(baseDir).exists()) {
                new File(baseDir).mkdirs();
            }

            // 当前时间
            String nowDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // 2. 补充上传者id, 实验代码名称, 创建时间
            String fileName = exp.getExpname() + "_" + exp.getUpid() + "_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".py";
            exp.setExpcodeurl(fileName);
            exp.setCreateTime(nowDate);

            // 3. 文件上传、数据库新增记录
            File newfile = new File(baseDir, fileName);
            file.transferTo(newfile);

            return ResultUtil.success();


        } catch (IOException e) {
            e.printStackTrace();
            return ResultUtil.unSuccess("异常");
        }
    }


    @RequestMapping(value = "/addExpStep1", method = RequestMethod.POST)
    @ResponseBody
    @ExceptionHandler
    public Result addUploadRecord1(Exp exp,
                                   @ApiParam(value = "上传文件", required = true) MultipartFile[] files, MultipartFile file, MultipartFile docFile, MultipartFile mp4File, Integer dataTag, HttpServletRequest request) {

//        Map<String,Object> resultMap = new HashMap<String,Object>();

        try {

            log.info(String.valueOf(exp));

//            log.info(String.valueOf(files.length));

            // 创建时间
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            exp.setCreateTime(time);
            if (dataTag == 0) { // 说明该实验没有对应的数据集文件
                exp.setFlag(1); // 设置该实验对应的 代码和数据集文件 数量
            } else {
                // 设置数据集文件存储目录，不存在则创建
                for (MultipartFile dataFile : files) {
                    while (!new File(ROOT_PATH + "/dataSets", Objects.requireNonNull(dataFile.getOriginalFilename())).exists()) {
                        dataFile.transferTo(new File(ROOT_PATH + "/dataSets", dataFile.getOriginalFilename()));
                    }
                }
                exp.setFlag(1 + files.length);
            }

            while (!new File(ROOT_PATH + "/dmspCode", Objects.requireNonNull(file.getOriginalFilename())).exists()) {
                file.transferTo(new File(ROOT_PATH + "/dmspCode", file.getOriginalFilename()));
            }
            exp.setExpcodeurl(file.getOriginalFilename());

            while (!new File(ROOT_PATH + "/videoAnewTest", Objects.requireNonNull(mp4File.getOriginalFilename())).exists()) {
                mp4File.transferTo(new File(ROOT_PATH + "/videoAnewTest", mp4File.getOriginalFilename()));
            }
            exp.setExpVideoPath(mp4File.getOriginalFilename());

            while (!new File(ROOT_PATH + "/DocFile/Doc", Objects.requireNonNull(docFile.getOriginalFilename())).exists()) {
                docFile.transferTo(new File(ROOT_PATH + "/DocFile/Doc", docFile.getOriginalFilename()));
            }
            exp.setExampleDoc(docFile.getOriginalFilename());


            // 新增试实验明细记录
            int index = expService.addExpStep(exp);

            log.info(String.valueOf(exp));


            if (index > 0) {
                if (dataTag == 1) {
                    for (MultipartFile dataFile : files) {
                        DataSet dataSet = new DataSet();
                        dataSet.setExpName(exp.getExpname());
                        dataSet.setExpId(exp.getId());
                        dataSet.setDataPath(dataFile.getOriginalFilename());
                        dataSet.setTid(exp.getUpid());
                        dataSet.setCreatime(time);
                        //将数据集插入数据库中
                        dataService.addDataSet(dataSet);
                    }
                }
                return ResultUtil.success();
//                resultMap.put("code",200);
//                resultMap.put("msg","上传成功");

            } else {
//                resultMap.put("code",300);
//                resultMap.put("msg","上传失败");
                return ResultUtil.unSuccess();
            }
//            return resultMap;


        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.unSuccess(String.valueOf(e));
//            resultMap.put("code",400);
//            resultMap.put("msg","异常失败");
//            return resultMap;
        }


    }


    @RequestMapping(value = "/addExpStep", method = RequestMethod.POST)
    @ResponseBody
    @ExceptionHandler
    public Result addUploadRecord(Exp exp,
                                  @ApiParam(value = "上传文件", required = true) MultipartFile[] files, MultipartFile[] file, MultipartFile docFile, Integer dataTag, HttpServletRequest request) {

//        Map<String,Object> resultMap = new HashMap<String,Object>();

        try {

            log.info(String.valueOf(exp));
            log.info(String.valueOf(file.length));
//            log.info(String.valueOf(files.length));

            // 创建时间
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            exp.setCreateTime(time);
            if (dataTag == 0) { // 说明该实验没有对应的数据集文件
                exp.setFlag(1); // 设置该实验对应的 代码和数据集文件 数量
            } else {
                // 设置数据集文件存储目录，不存在则创建
                for (MultipartFile dataFile : files) {
                    while (!new File(ROOT_PATH + "/dataSets", dataFile.getOriginalFilename()).exists()) {
                        dataFile.transferTo(new File(ROOT_PATH + "/dataSets", dataFile.getOriginalFilename()));
                    }
                }
                exp.setFlag(1 + files.length);
            }


            int k = 0; // 记录 代码、视频和案例 是否都存储成功
            for (MultipartFile otherFile : file) {
                if (k == 3) {
                    break;
                }
                String extension = otherFile.getOriginalFilename().substring(otherFile.getOriginalFilename().lastIndexOf(".") + 1);
                if (extension.equals("py")) {
                    while (!new File(ROOT_PATH + "/dmspCode", otherFile.getOriginalFilename()).exists()) {
                        otherFile.transferTo(new File(ROOT_PATH + "/dmspCode", otherFile.getOriginalFilename()));
                    }
                    exp.setExpcodeurl(otherFile.getOriginalFilename());
                    k = k + 1;
                    continue;
                }
                if (extension.equals("mp4")) {
                    while (!new File(ROOT_PATH + "/videoAnewTest", otherFile.getOriginalFilename()).exists()) {
                        otherFile.transferTo(new File(ROOT_PATH + "/videoAnewTest", otherFile.getOriginalFilename()));
                    }
                    exp.setExpVideoPath(otherFile.getOriginalFilename());
                    k = k + 1;
                    continue;
                }
                if (extension.equals("docx")) {
                    while (!new File(ROOT_PATH + "/DocFile/Doc", otherFile.getOriginalFilename()).exists()) {
                        otherFile.transferTo(new File(ROOT_PATH + "/DocFile/Doc", otherFile.getOriginalFilename()));
                    }
                    exp.setExampleDoc(otherFile.getOriginalFilename());
                    k = k + 1;
                }
            }


            // 新增试实验明细记录
            int index = expService.addExpStep(exp);

            log.info(String.valueOf(exp));


            if (index > 0) {
                if (dataTag == 1) {
                    for (MultipartFile dataFile : files) {
                        DataSet dataSet = new DataSet();
                        dataSet.setExpName(exp.getExpname());
                        dataSet.setExpId(exp.getId());
                        dataSet.setDataPath(dataFile.getOriginalFilename());
                        dataSet.setTid(exp.getUpid());
                        dataSet.setCreatime(time);
                        //将数据集插入数据库中
                        dataService.addDataSet(dataSet);
                    }
                }
                return ResultUtil.success();
//                resultMap.put("code",200);
//                resultMap.put("msg","上传成功");

            } else {
//                resultMap.put("code",300);
//                resultMap.put("msg","上传失败");
                return ResultUtil.unSuccess();
            }
//            return resultMap;


        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.unSuccess(String.valueOf(e));
//            resultMap.put("code",400);
//            resultMap.put("msg","异常失败");
//            return resultMap;
        }


    }


    @ApiOperation(value = "新增实验", notes = "1. 设置上传路径; 2. 补充上传者id, 实验代码名称, 创建时间; 3. 文件上传、数据库新增记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "实验名称", name = "expname", type = "query", required = true),
            @ApiImplicitParam(value = "实验目的", name = "exptarget", type = "query", required = true),
            @ApiImplicitParam(value = "实验任务", name = "expwork", type = "query", required = true),
            @ApiImplicitParam(value = "上传者id", name = "upid", type = "query", required = true),
            @ApiImplicitParam(value = "文件个数", name = "flag", type = "query", required = true)
    })
    @ResponseBody
    @PostMapping(value = "/testExp1")
    public Result uploadFile1(@RequestParam("file") MultipartFile file, Exp exp, HttpSession session) {


//        log.info(file.getOriginalFilename());
        String dir;
        String fileName;
        // 将新增exp和dataSet的时间设置一致，可以根据时间和tid匹配到数据集
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        int type;
        int num = 0;

        if (FilenameUtils.getExtension(file.getOriginalFilename()).equals("py")) {
            type = 0;
            dir = "/dmspCode";
            fileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "_" + file.getOriginalFilename();
        } else {
            type = 1;
            dir = "/dmspData";
            fileName = file.getOriginalFilename();
        }

        // 1. 设置上传路径
        String baseDir = ROOT_PATH + dir;
        if (!new File(baseDir).exists()) {
            new File(baseDir).mkdirs();
        }


        // 3. 文件上传、数据库新增记录
        File newfile = new File(baseDir, fileName);
        try {
            //将文件上传到指定路径，并以新的名字命名
            file.transferTo(newfile);
            //在实验数据表中添加相应的一行信息
            if (type == 0) {
                // 2.  实验代码名称, 创建时间;
                exp.setExpcodeurl(fileName);
                exp.setCreateTime(createTime);

                num = expService.addExp(exp);
//                log.info(String.valueOf(exp));
//                ((User)session.getAttribute("loginUser")).setNewExpId(exp.getId());
            } else {
                DataSet dataSet = new DataSet();
                dataSet.setTid(exp.getUpid());
                dataSet.setDataPath(fileName);
                dataSet.setExpName(exp.getExpname());
                dataSet.setCreatime(createTime);

//                dataSet.setExpId(((User)session.getAttribute("loginUser")).getNewExpId());
//                num = dataService.uploadSelfData(dataSet);

                num = dataService.addDataSet(dataSet);
            }

            if (num > 0) {
                return ResultUtil.success("上传成功");
            } else {
                return ResultUtil.unSuccess("创建失败");
            }
        } catch (Exception e) {
            return ResultUtil.unSuccess("创建失败");
        }


    }


    @ResponseBody
    @RequestMapping(value = "/testExp", method = {RequestMethod.POST, RequestMethod.GET})
    public Result uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        log.info("文件上传");
        String filename = file.getOriginalFilename();
        String name = file.getName();

        //1、传入固定路径
        String newPath = ROOT_PATH + "/dmspData";
        if (!new File(newPath).exists()) {
            new File(newPath).mkdirs();
        }
        try {
//            file.transferTo(new File(newPath, filename));
            return ResultUtil.unSuccess();

        } catch (Exception e) {
            return ResultUtil.unSuccess();
        }


    }


    @ApiOperation(value = "代码执行", notes = "1. 将前端编辑器中的代码编码传递到后台; 2. 判断存储历史代码的目录是否存在，不存在则创建; 3. 历史代码写入文件; 4. 代码运行,记录结果并写入到指定文件; 5. 数据库新增记录")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "实验id", name = "expId", type = "query", required = true)
    })

    /**
     * compile:正则匹配表达式
     * context:待匹配的字符串
     * start:匹配到的字段替换成的新字段的前半部分
     * end:匹配到的字段字换成的新字段的后半部分
     */
    public Map<String, String> Replace(String compile, String context, String start, String end) {
        Map<String, String> map = new HashMap<String, String>();
        Pattern p = Pattern.compile(compile);
        Matcher m = p.matcher(context);
        StringBuffer sb = new StringBuffer();
        boolean result = m.find();
        int i = 0;
        while (result) {//如果匹配成功就替换
            m.appendReplacement(sb, start + "_" + i + end);
            result = m.find();//继续下一步匹配
            i = i + 1;
        }
        m.appendTail(sb);
        map.put("newContext", sb.toString());
        map.put("count", String.valueOf(i));

        return map;

    }

    @PostMapping("/runEditor")
    @ResponseBody
    public Result runeditor(@ApiParam(value = "代码", required = true, name = "context", type = "query") String
                                    context,
                            @ApiParam(value = "实验id", required = true, name = "expId") @RequestParam String expId, HttpSession
                                    session) {
        try {

            History history = new History();
            // 代码解码
            context = java.net.URLDecoder.decode(context, "UTF-8");


//            log.info(context);
            User user = (User) session.getAttribute("loginUser");

            // 获取当前日期，格式为年月日
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            String currentDate = dateFormat.format(new Date());
            String currentDir = "/stuCode/" + currentDate + "/" + user.getUsername() + "/";

            // 执行代码、结果保存路径
            String parentDir = ROOT_PATH + currentDir;
            if (!new File(parentDir).exists()) {
                new File(parentDir).mkdirs();
            }
            // 历史代码文件名
            String RandomFilename = new SimpleDateFormat("HHmmss").format(new Date());

            history.setExpId(Integer.valueOf(expId));
            history.setSid(user.getId());
            history.setSname(user.getUsername());
            history.setCodeurl(currentDir + RandomFilename + "_code.py");
            history.setResulturl(currentDir + RandomFilename + "_result.py");


            // 写入历史代码到文件，用户输入的代码
            BufferedWriter out = new BufferedWriter(new FileWriter(parentDir + RandomFilename + "_code.py"));
            // 写入历史代码到文件，如果用户输入的代码中包含图片、csv文件，则将保存路径处理后
            BufferedWriter out1 = new BufferedWriter(new FileWriter(parentDir + RandomFilename + "_0_code.py"));
            out.write(context);
            out.close();

//            String newContext = Replace("show\\(.*?\\)", context, "F:/project/Anewdemo/stuCode/" + user.getUsername() + "/" + RandomFilename + "_fig");
//            String newContext = Replace("show\\(.*?\\)", context, "/opt/pubWebPro/stuCode/" + user.getUsername() + "/" + RandomFilename + "_fig");
//            String baseDir = ROOT_PATH+ "/stuCode/";

            String figCompile = "show\\(.*?\\)";
            String figPicName = parentDir + RandomFilename + "_fig";
            Map<String, String> figContext = Replace(figCompile, context, "savefig('" + figPicName, ".png')");
            String figCount = figContext.get("count");

            String csvCompile = "\\.to_csv\\(.+['|\"]";
            String csvPicName = parentDir + RandomFilename + "_csv";
            Map<String, String> csvContext = Replace(csvCompile, figContext.get("newContext"), ".to_csv('" + csvPicName, ".csv'");
            String figCsvContext = csvContext.get("newContext");
            String csvCout = csvContext.get("count");

            out1.write(figCsvContext);
            out1.close();

            // 记录运行结果
            StringBuilder result = new StringBuilder();
            // 执行python代码
            Process pr = Runtime.getRuntime().exec("python " + parentDir + RandomFilename + "_0_code.py");
            // 记录执行结果
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(pr.getInputStream(), "GB2312"));
            // 记录执行中出现的错误
            BufferedReader isError = new BufferedReader(new InputStreamReader(pr.getErrorStream(), "GB2312"));
            // 错误变量
            String errorline;
            // 输出变量
            String line;
            Integer tag = null;

            // 判断运行错误是否有值
            if ((errorline = isError.readLine()) != null) {
                tag = 1;
                result.append("代码运行有误，请检查后重新运行！！！\n");
                result.append(errorline).append('\n');
                //遍历错误值，并写入结果
                while ((errorline = isError.readLine()) != null) {
                    result.append(errorline).append('\n');
                }
                isError.close();

                //判断输出变量里面值
                if ((line = in.readLine()) != null) {
//                    result.append("代码部分运行有误!!!");
                    result.append(line).append('\n');
                    //遍历输出值，并写入
                    while ((line = in.readLine()) != null) {
                        result.append(line).append('\n');
                    }
                } else {
                    in.close();
                    pr.waitFor();
                }
            } else {
                // 错误变量里面没有值，遍历输出变量的值
                if ((line = in.readLine()) == null) {
                    tag = 0;
                    result.append("代码没有输出");
                } else {
                    tag = 1;
                    result.append(line).append('\n');
                    while ((line = in.readLine()) != null) {
                        result.append(line).append('\n');
                    }
                }

            }
            // 代码运行结果写入结果文件中
//            writeResult(result, path, RandomFilename);
            BufferedWriter outResult = new BufferedWriter(new FileWriter(parentDir + RandomFilename + "_result.py"));
            outResult.write(String.valueOf(result));
            outResult.close();

            history.setCreatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            int cf = 0;
            int pf = 0;
            for (int c = 0; c < Integer.parseInt(csvCout); c++) {
                if (new File(parentDir + RandomFilename + "_csv_" + c + ".csv").exists()) {
                    cf++;
                }
            }
            for (int p = 0; p < Integer.parseInt(figCount); p++) {
                if (new File(parentDir + RandomFilename + "_fig_" + p + ".png").exists()) {
                    pf++;
                }
            }
            history.setFigCount(pf);
            history.setCsvCount(cf);
//            history.setFigCount(Integer.valueOf(figCount));
//            history.setCsvCount(Integer.valueOf(csvCout));

            RunResult runResult = new RunResult();
            runResult.setTag(String.valueOf(tag));
            runResult.setFigCount(pf);
            runResult.setCsvCount(cf);

//            i = 0;
//            runResult.setFigName(RandomFilename + "_fig");
            runResult.setFigName("/"+currentDate + "/" + user.getUsername() + "/" + RandomFilename);
//            runResult.setCodeName("stuCode/" + user.getUsername() + "/" + RandomFilename + "_result.py");
            runResult.setCodeName(currentDir + RandomFilename + "_result.py");

            // 历史记录表中添加一条新的记录
            int num = expService.addHistory(history);
            if (num > 0) {
                return ResultUtil.success(runResult);
            } else {
                return ResultUtil.unSuccess();
            }

        } catch (Exception e) {
            return ResultUtil.error(e);
        }
    }

    @GetMapping("/expDataSets/{expId}")
    @ResponseBody
    public Result getExpDataSETS(@PathVariable Integer expId) {
        try {
            Result result = new Result();
            List<Exp> expDatas = expService.getExpDataSets(expId);
            result.setTotal(expDatas.size());
            result.setDatas(expDatas);
            result.setCode(200);
            return ResultUtil.success(result);

        } catch (Exception e) {
            return ResultUtil.unSuccess(String.valueOf(e));
        }


    }


}
