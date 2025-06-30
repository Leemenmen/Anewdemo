package com.newdmsp.demo.controller;

import com.newdmsp.demo.service.ShareService;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import com.newdmsp.demo.utils.WordRead;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static com.newdmsp.demo.utils.Config.ROOT_PATH;

@Slf4j
@Controller
@Api(tags = "文件管理")
public class FileController {

    @Resource
    ShareService shareService;

    /**
     * 读取指定路径的内容并返回到前端
     *
     * @param filepath
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "读取代码", notes = "1. 路径解码； 2. 与当前目录拼接；3. 读取文件")
    @PostMapping("/readCode")
    @ResponseBody
    public Result readCode(@ApiParam(value = "文件路径", required = true, defaultValue = "dmspCode/数据获取爬虫代码示范.py") @RequestParam("codePath") String filepath) throws IOException {

        // 传入路径
        String path = URLDecoder.decode(filepath, "UTF-8");
//        log.info(filepath);
//        log.info(path);

        // 当前系统路径
        String baseDir = ROOT_PATH;
//        log.info(baseDir);
        // 拼接
        File readCode = ResourceUtils.getFile(baseDir + "/" + path);
//        log.info("读取代码路径");
//        log.info(String.valueOf(readCode));

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(readCode))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {

                result.append(line + '\n');
            }
            bufferedReader.close();
            return ResultUtil.success("读取成功", result);
        } catch (Exception e) {
            return ResultUtil.unSuccess("读取失败");
        }

    }

    /**
     * 下载指定路径的文件
     *
     * @param file
     * @param response
     */
    @ApiOperation(value = "文件下载",
            notes = "1. codepath(示例代码) 和 uploadepath(共享文件) 若是都不为空则默认下载的是 示例代码；" +
                    "2. 若下载共享文件，则需要将除 codepath 的其他字段都要填充" +
                    "3. 拼接文件路径； 3.1 直接下载； 3.2 若请求的是 共享文件，访问数据库将该文件count加1")
    @RequestMapping(value = "/downCode", method = RequestMethod.GET)
    public void download2(@ApiParam(value = "示例代码名称", required = true, defaultValue = "数据获取爬虫代码示范.py") @RequestParam("codepath") String file,
                          @ApiParam(value = "共享代码名称, 2022_06_16/2_095941_20220615160226_c.py", defaultValue = "null") @RequestParam("uploadpath") String upload,
                          @ApiParam(value = "共享代码id", defaultValue = "0") @RequestParam String id,
                          @ApiParam(value = "共享代码下载次数", defaultValue = "0") @RequestParam String count,
                          @ApiParam(value = "共享是0代码还是1数据", defaultValue = "0") @RequestParam String flag,
                          HttpServletResponse response) throws UnsupportedEncodingException {

        String filePath = "";
        String newfile = "";
        int signal = 0;
        // 1.拼接文件路径
        if ("null".equals(file)) {
            newfile = java.net.URLDecoder.decode(upload, "UTF-8");
            if ("0".equals(flag)) {
                filePath = ROOT_PATH + "/shareFile/code/" + newfile;
            } else {
                filePath = ROOT_PATH + "/shareFile/data/" + newfile;
            }
            signal = 1;
        } else {

            filePath = ROOT_PATH + "/dmspCode/" + file;
            newfile = file;
        }
//        log.info("下载路径");
//        log.info(filePath);
        // 2.1 直接下载
        try {
            InputStream bis = new BufferedInputStream(new FileInputStream(filePath));
            String fileName = newfile;
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();

            }

            // 2.2 若请求的是文件是 共享文件，访问数据库将该文件count加1
            if (signal == 1 && Integer.parseInt(id) > 0) {

                shareService.updateShare(Integer.valueOf(id), Integer.parseInt(count) + 1);
            }
            out.close();
            bis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ApiOperation(value = "读取Excel数据文件", notes = "1. 路径解码并拼接；2，读取并返回excel")
    @RequestMapping(value = "/readExcel", method = RequestMethod.GET)
    @ResponseBody
    public Result readExcel(@ApiParam(value = "数据文件路径", required = true, defaultValue = "shareFile/data/2022_06_16/2_095509_Trust_CAR.xlsx") @RequestParam("uploadPath") String upload) throws UnsupportedEncodingException {


        String filePath = ROOT_PATH + "/" + java.net.URLDecoder.decode(upload, "UTF-8");
//        log.info(filePath);
        try {

            Workbook book;
            Sheet sheet = null;
            InputStream inputStream = new FileInputStream(new File(filePath));
            book = WorkbookFactory.create(inputStream);
            sheet = book.getSheetAt(0);


            if (sheet != null) {
                // 创建集合，保存每一行的每一列
                List<String> list = new ArrayList<>();
                // 获取第一行
                int firstRow = sheet.getFirstRowNum();
                // 获取最后一行
                int lastRow = sheet.getLastRowNum();
                list.add(String.valueOf(lastRow));
                // 循环行数依次获取列数
                for (int i = firstRow; i < lastRow + 1; i++) {
                    // 获取第 i 行
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        // 获取此行的第一列
                        int firstCell = 0;

                        // 获取此行的存在数据的最后一列
                        int lastCell = row.getLastCellNum();

                        for (int j = firstCell; j < lastCell; j++) {

                            // 获取第 j 列
                            Cell cell = row.getCell(j);
                            if (cell != null) {
                                list.add(cell.toString());
                            } else {
                                list.add("");
                            }
                        }
                    }
                }
                return ResultUtil.success(list);
            }
            inputStream.close();
            return ResultUtil.unSuccess("文件格式错误");

        } catch (IOException e) {

            return ResultUtil.unSuccess("读取失败，稍后重试");

        }

    }


    @ApiOperation(value = "读取Csv数据文件", notes = "1. 路径解码并拼接；2，读取并返回")
    @RequestMapping(value = "/readCsv", method = RequestMethod.GET)
    @ResponseBody
    public Result readCsv(@ApiParam(value = "数据文件路径", required = true, defaultValue = "shareFile/data/2022_06_16/2_095509_Trust_CAR.csv") @RequestParam("csvPath") String csvPath) throws IOException, CsvValidationException {

        String filePath = ROOT_PATH + java.net.URLDecoder.decode(csvPath, "UTF-8");
        int i = 0;
        String record;
        String strJoin = null;
        String strHead = null;
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath));
             CSVReader csvReader = new CSVReader(reader)) {
            while (!(record = Arrays.toString(csvReader.readNext())).equals("null")) {
                if (i == 0) {
                    strHead = record;
                } else {
                    if (strJoin == null) {
                        strJoin = record;
                    } else {
                        strJoin = strJoin + "," + record;
                    }
                }
                i = i + 1;
            }
            reader.close();
            csvReader.close();
        }

        String[] newStrJoin = deleteChar(strJoin);
        String[] newHead = deleteChar(strHead);
        int cl = newStrJoin.length / (i - 1);
        String[][] array = new String[i][cl];

        int flag = cl - newHead.length;
        if (flag == 0) {
            array[0] = newHead;
        } else {
            for (int col = 0; col < cl; col++) {
                if (col < flag) {
                    array[0][col] = " ";
                } else {
                    array[0][col] = newHead[col - flag];
                }
            }
        }

        for (int row = 1; row < i; row++) {
            for (int col = 0; col < cl; col++) {
                array[row][col] = newStrJoin[(row - 1) * cl + col];
            }
        }


        Result result1 = new Result();
        result1.setData(array);
        result1.setTotal(i);
        result1.setCode(200);
        return result1;


    }


    @ApiOperation(value = "读取Csv数据文件", notes = "1. 路径解码并拼接；2，读取流并返回")
    @RequestMapping(value = "/readCsvLiu", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> readCsvLiu(@RequestParam("csvPath") String csvPath) {

        try {
            String filePath = ROOT_PATH + java.net.URLDecoder.decode(csvPath, "UTF-8");
            log.info(filePath);
            Map<String, Object> result = new HashMap<String, Object>();
            File file = new File(filePath);
            //获取文件名
            file.setReadable(true);
            file.setWritable(true);
            InputStreamReader isr = null;
            BufferedReader br = null;

            isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            br = new BufferedReader(isr);

            String line = "";
            ArrayList<String> records = new ArrayList<>();

            while ((line = br.readLine()) != null) {
//                System.out.println(line);
                records.add(line);
            }

//            System.out.println(records);
//            System.out.println("表格读取行数：" + records.size());



            result.put("code", 200);
            result.put("line", records.size());
            result.put("datas", records);
            isr.close();
            br.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static String[] deleteChar(String strJoin) {
        char[] arr1 = strJoin.toCharArray(); //string拆封成字符数组
        String newStrJoin = null;
        for (int j = 0; j < (arr1.length - 1); j++) { //遍历字符数组删除不需要的字符
            if (arr1[j] == ']' || arr1[j] == '[' || arr1[j] == ' ' || (arr1[j] == ',' && newStrJoin == null)) {
                continue;
            } else {
                if (newStrJoin == null) {
                    newStrJoin = String.valueOf(arr1[j]);
                } else {
                    newStrJoin = newStrJoin + arr1[j];
                }
            }
        }
        return newStrJoin.split(",");
    }


    @GetMapping("/downloadResult")
    public void download2(@RequestParam("path") String file, @RequestParam("type") String type, HttpServletResponse response) throws UnsupportedEncodingException {

        try {

//            log.info(file);
            String filePath = ROOT_PATH + URLDecoder.decode(file, "UTF-8");
            ;
//            log.info(filePath);
            InputStream bis = new BufferedInputStream(new FileInputStream(new File(filePath)));
            String fileName = file;
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentType("multipart/form-data");
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            if (Integer.parseInt(type) == 1) {
                out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
            }
            int len = 0;
            while ((len = bis.read()) != -1) {
                out.write(len);
                out.flush();
            }
            out.close();
            bis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @ResponseBody
    @PostMapping("/multiUpload")
    public Result devUpload(@RequestParam("files") MultipartFile[] files, MultipartFile file, HttpServletRequest request) {
//        log.info(file.getOriginalFilename());
        //datafile.getOriginalFilename()
        for (MultipartFile reFile : files) {
//            log.info(reFile.getOriginalFilename());
        }

        return ResultUtil.success();

    }

    @ResponseBody
    @GetMapping("/readDocFile")

    /**
     * 读取word中的文本内容（段落、表格、图片分开处理）转HTML docx后缀名的Word
     * @param filePath 文件路径
     * @throws IOException
     */
    public Map<String, Object> readWordFile3(@RequestParam String path) throws UnsupportedEncodingException {
        Map<String, Object> result = new HashMap<String, Object>();
        //word文件地址放在src/main/webapp/下
        //表示到项目的根目录（webapp）下，要是想到目录下的子文件夹，修改"/"即可
        String filePath = java.net.URLDecoder.decode(ROOT_PATH+"/DocFile/doc/"+path, "UTF-8");
        try {
            //读取Word中的文本内容包含表格
            String wordhtml = WordRead.readWordImgToHtml(filePath);
            result.put("content", wordhtml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
