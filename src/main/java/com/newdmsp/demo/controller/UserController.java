package com.newdmsp.demo.controller;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import com.newdmsp.demo.entity.*;
import com.newdmsp.demo.entity.Record;
import com.newdmsp.demo.security.RsaUtil;
import com.newdmsp.demo.security.user.CustomUserDetailsService;
import com.newdmsp.demo.service.ExpService;
import com.newdmsp.demo.service.UserService;
import com.newdmsp.demo.utils.Config;
import com.newdmsp.demo.utils.PageModel;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import io.swagger.annotations.*;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@Api(tags = "用户管理")
public class UserController {
    @Resource
    UserService userService;
    @Resource
    ExpService expService;
//    @Resource
//    RsaUtil rsaUtil;


    @ApiOperation(value = "用户登录2. 获取公钥", notes = "1.返回公钥到前端；2.利用公钥对密码进行加密")
    @GetMapping("/login/get/pub-key")
    @ResponseBody
    public String getPublicKey(){
//        session.setAttribute("publicKey", rsa.getPublicKey());
//        log.info((String) session.getAttribute("publicKey"));
//        if (session.getAttribute("publicKey")!=null){
//            return ResultUtil.success();
//        }else {
//            return ResultUtil.unSuccess();
//        }
        return RsaUtil.getPublicKey();
    }

    @ApiOperation(value = "登录", notes = "1. 输入用户名和密码提交，密码在前端加密，直接使用url登录未想好如何实现；2.用户基本信息存入session；3.若角色是学生，session中存入其班级名称和实验信息")
    @PostMapping("/loginOrig")
    public void login(@ApiParam(value = "用户名", type = "body", required = true) String username,
                      @ApiParam(value = "密码", type = "body", required = true) String password){

//        String publicKey = getPublicKey();
//        RSA rsa = new RSA(null, publicKey);
//        String pwd = new String(rsa.encrypt(password, KeyType.PublicKey));
//        log.info(username);
//        log.info(pwd);
//        return "forward:/loginTrue?username="+username+"&password="+pwd+"";
    }


    /**
     * 实现实验指定中的操作栏功能，点击按钮，则展示该实验在老师所教授班级的授权情况
     * 1.获取老师的所有班级
     * 2.获取该老师所教授班级被授予指定实验的班级条目
     * 3.若条目数量为0，则直接返回1中的列表
     * 4.若条目数量不为0，则获取条目的详细信息吗，并将该信息填充1中的对应字段
//     * @param tgrade
     * @param session
     * @return
     */
    @ApiOperation(value = "教师 获取班级实验授权情况", notes = "1. 取session中loginUser; 2.获取该实验id对应的班级数目; 3.获取教师所有班级列表; 4.1 若2中数据为0，将3中列表返回到前端，所有班级该实验都是未授权状态")
    @ResponseBody
    @GetMapping("/tGrades/{expId}")
    public Result getTgrades(@ApiParam(value = "实验id", required = true)@PathVariable Integer expId, HttpSession session) {
//        log.info(String.valueOf(expId));

        // 1. 取session中loginUser
        User loginUser = (User) session.getAttribute("loginUser");

        List<Tgrade> tgrades = null; // 存放教师教授班级列表
        List<Tgrade> uptgrades = null; // 存放 教师教授班级和指定实验列表
        // 2.获取该实验id对应的班级 是该教师教授班级 列表
        uptgrades = userService.getGtoeTgrades(expId,loginUser.getId());
//        int num = userService.getNumGtoe(expId);

        // 3.获取教师所有班级列表
        tgrades = userService.getGrades(loginUser.getId());

        // 4.1 若2中长度为0，将3中列表返回到前端，所有班级该实验都是未授权状态
        if (uptgrades.size() == 0) {
            return ResultUtil.success(tgrades);
        } else {
            // 4.2 依次对比2中每条条目，和3中每条条目，对3中班级id与2中相同的条目设置 实验id 和 班级实验对照表的id
            for (Tgrade upgrade:uptgrades){
                for (Tgrade grade:tgrades){
                    if (upgrade.getGradeid().equals(grade.getGradeid())){
                        grade.setExpid(expId);
                        grade.setGeid(upgrade.getGeid());
                        break;
                    }
                }
            }
//             log.info("==替换后=:"+tgrades);
            return ResultUtil.success(tgrades);
        }
    }

    @ResponseBody
    @RequestMapping("/cancel")
    public Result delTgrade(HttpServletRequest request) {
        Integer id = Integer.valueOf(request.getParameter("id"));
        try {
            int num = userService.delTgrade(id);
//            log.info("===num==" + num);
            if (num > 0) {
                return ResultUtil.success();
            } else {
                return ResultUtil.unSuccess();
            }
        } catch (Exception e) {
            return ResultUtil.error(e);
        }

    }

    @ResponseBody
    @RequestMapping("/addGtoe")
    public Result addTgrade(Tgrade tgrade) {

//        log.info("===tgrade==:"+tgrade);
        userService.addGtoe(tgrade);
//        log.info("===newtgrade==:"+tgrade);
        if (tgrade.getId() > 0) {
            return ResultUtil.success();
        } else {
            return ResultUtil.unSuccess();
        }

    }


    @ApiOperation(value = "获取教师教授班级", notes = "不用输入参数，根据session中登录用户id获取")
    @GetMapping("/getGrades")
    @ResponseBody
    public Result getGrades(HttpSession session){

        try {
            User user = (User) session.getAttribute("loginUser");
            List<Tgrade> tgrade = userService.getGrades(user.getId());
//        log.info("==grade=="+tgrade);
            return ResultUtil.success(tgrade);
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }

    @ApiOperation(value = "查询班级学生", notes = "1. 默认显示该班级的所有学生; 2. 可以通过输入学号或姓名查找指定学生")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学生学号或姓名，可以不写", name = "username", type = "query",defaultValue = "null"),
            @ApiImplicitParam(value = "班级id", name = "gradeid", type = "query",  defaultValue = "1", required = true)
    })
    @GetMapping("/users/{pageNo}/{mPageSize}")
    @ResponseBody
    public Result getUsers(String username, Integer gradeid,
                           @ApiParam(value = "页码", type = "path", required = true) @PathVariable int pageNo,
                           @ApiParam(value = "每页显示几条数据", type = "path", required = true) @PathVariable int mPageSize){

        try {
            User user = new User();
            user.setGradeid(gradeid);
            if (username.length()>0 & !username.equals("null")){
                user.setUsername(username);
            }else {
                user.setUsername("null");
            }
            PageModel model = new PageModel<>(pageNo, user);
            model.setPageSize(mPageSize);
            List<User> tss = userService.getUsersByGradePage(model);
            if (tss.size() >= 0) {
                Result<History> result = ResultUtil.success(tss);
                result.setTotal(userService.getUsersByGradePageTotals(model));
                if (result.getTotal() == 0) {
                    result.setMsg("没有查到学生数据");
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


//    @ApiOperation(value = "查询班级学生", notes = "1. 默认显示该班级的所有学生; 2. 可以通过输入学号或姓名查找指定学生")
//    @ApiImplicitParams({
//            @ApiImplicitParam(value = "学生学号或姓名，可以不写", name = "username", type = "query",defaultValue = "1"),
//            @ApiImplicitParam(value = "班级id", name = "gradeid", type = "query", defaultValue = "1", required = true)
//    })
//    @PostMapping("/users")
//    @ResponseBody
//    public Result getUsers(User user){
////        log.info("==传入的user==="+user);
//        try {
//            log.info("==传入的user==="+user);
//            List<User> users = userService.getUsers(user);
//            return ResultUtil.success(users);
//        }catch (Exception e){
//            return ResultUtil.unSuccess();
//        }
//
//    }



    @ApiOperation(value = "重置学生密码", notes = "根据学生id")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学生id", name = "id", required = true, type = "query")
    })
    @PostMapping("/reset")
    @ResponseBody
    public Result resetPasswprd(String id){

        try {
            User user = new User();
            user.setId(Integer.valueOf(id));
            int num = userService.updateUser(user);
            if (num>0){
                return ResultUtil.success();
            }else {
                return ResultUtil.unSuccess();
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }

    @ApiOperation(value = "禁用学生账号", notes = "根据学生id")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学生id", name = "id", required = true, type = "query")
    })
    @PostMapping("/banStu")
    @ResponseBody
    public Result banStu(String id){

        try {
            User user = new User();
            user.setId(Integer.valueOf(id));
            user.setEnable(0);
            user.setPassword("123");
            int num = userService.banStu(user);
            if (num>0){
                return ResultUtil.success();
            }else {
                return ResultUtil.unSuccess();
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }

    @ApiOperation(value = "禁用学生账号", notes = "根据学生id")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学生id", name = "id", required = true, type = "query")
    })
    @PostMapping("/unBan")
    @ResponseBody
    public Result unbanStu(String id){

        try {
            User user = new User();
            user.setId(Integer.valueOf(id));
            user.setPassword("123");
            user.setEnable(1);
            int num = userService.banStu(user);
            if (num>0){
                return ResultUtil.success();
            }else {
                return ResultUtil.unSuccess();
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }


    @ApiOperation(value = "新增学生")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "学号", name = "username", required = true, type = "query"),
            @ApiImplicitParam(value = "姓名", name = "realname", required = true, type = "query"),
            @ApiImplicitParam(value = "班级id", name = "gradeid", required = true, type = "query")
    })
    @PostMapping("/addUser")
    @ResponseBody
    public Result addUser(String username, String realname, String gradeid){

        try {
            User user = new User();
            user.setUsername(username);
            user.setRealname(realname);
            user.setGradeid(Integer.valueOf(gradeid));
            int num = userService.addUser(user);
            if (num>0){
                return ResultUtil.success();
            }else {
                return ResultUtil.unSuccess();
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }


    @ApiOperation(value = "修改密码", notes = "1. 用户前端修改密码为强密码； 2. 前端提示框请用户确认密码； 3.前端密码用公钥加密； 4.后端私钥解密后存入数据库；")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "id", required = true, type = "query"),
            @ApiImplicitParam(value = "公钥加密后强密码", name = "password", required = true, type = "query")
    })
    @PostMapping("/updateStuPassword")
    @ResponseBody
    public Result updateStuPass(String id, String password){

        try {
            User user = new User();
            user.setId(Integer.valueOf(id));
            String updatePass = RsaUtil.decrypt(password);
            user.setPassword(updatePass);
            int num = userService.updateStu(user);
            if (num>0){
                return  ResultUtil.success();
            }else {
                return ResultUtil.unSuccess();
            }
        }catch (Exception e){
            return ResultUtil.unSuccess();

        }
    }

//    @ResponseBody
//    @RequestMapping("/loginTrue")
//    public Result loginTrue(User user){
//        log.info(String.valueOf(user));
//        customUserDetailsService.loadUserByUsername(user.getUsername());
//        return ResultUtil.success();
//    }

    @ResponseBody
    @RequestMapping("/login1.do")
    public Result Index(User user, HttpServletRequest request, HttpSession session,HttpServletResponse response) {
        boolean userIsExisted = userService.userIsExisted(user);
        log.info(String.valueOf(user));
        //将token存入Http的header中
        System.out.println(userIsExisted + " - " + request.getHeader("token"));
        user = getUserInfo(user);
//        System.out.println("=========："+user);
        if (userIsExisted && user != null) {
            user = setSessionUser(user, request.getSession());
            //将当前用户信息存入cookie
//            setCookieUser(request,response,session);
            if (user.getRoleid()==1){
                Tgrade sgrade = userService.getStuGrade(user.getGradeid());
//                log.info("==sgrade==="+sgrade);
                user.setGname(sgrade.getGname());
            }
            log.info(String.valueOf(user));
            session.setAttribute("loginUser", user);
//            System.out.println("=====存入session的信息====：" + user);
            return ResultUtil.success("登录成功", user);
        } else {
            return ResultUtil.unSuccess("用户名或密码错误");
        }
    }


    /**
     * 教师功能
     * 作业评阅 > 班级 > 实验 (simple_check.html)
     * 界面显示班级的所有学生，以及实验报告提交状态
     * @return
     */
    @ApiOperation(value = "教师获取学生实验报告")
    @ResponseBody
    @GetMapping("/getGradeRecordsByExp")
    public Result getGtoe(@ApiParam(value = "实验id", required = true, name = "expid") String expid,
                          @ApiParam(value = "班级id", required = true, name = "gid") String gid){

        try {
            Record record = new Record();
            record.setExpid(Integer.valueOf(expid));
            record.setGid(Integer.valueOf(gid));
            List<Record> records = userService.getGtoe(record);
            return ResultUtil.success(records);
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }


    /**
     * 教师功能
     * 作业评阅 > 班级 > 实验 (simple_check.html)
     * 界面显示班级的所有学生，以及实验报告提交状态
     * @return
     */
    @ApiOperation(value = "教师获取学生实验报告以及打分情况")
    @ResponseBody
    @GetMapping("/getGradeRecordsAndScoreByExp")
    public Result getScoreGtoe(@ApiParam(value = "实验id", required = true, name = "expid") String expid,
                          @ApiParam(value = "班级id", required = true, name = "gid") String gid){

        try {
            Record record = new Record();
            record.setExpid(Integer.valueOf(expid));
            record.setGid(Integer.valueOf(gid));
            List<Record> records = userService.getGtoe(record);
            return ResultUtil.success(records);
        }catch (Exception e){
            return ResultUtil.unSuccess();
        }

    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        delCookieUser(request, response,session);
        request.getSession().removeAttribute(Config.CURRENT_USERNAME);
        return "login";
    }

    /**
     * 通过用户信息获取用户权限信息，并存入session中
     *
     * @param user
     * @param session
     * @return
     */
    public User setSessionUser(User user, HttpSession session) {

        List<Exp> exps = expService.getExpsByGradeId(user.getGradeid());
        user.setExps(exps);
        session.setAttribute(Config.CURRENT_USERNAME, user);
//        log.info(Config.CURRENT_USERNAME+"==="+session.getAttribute(Config.CURRENT_USERNAME));
        return user;

    }

    /**
     * 登录时将用户信息加入cookie中
     *
     * @param response
     */
    private void setCookieUser(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        User sessionUser = (User) session.getAttribute(Config.CURRENT_USERNAME);
        sessionUser.setPassword(null);
        Cookie cookie = new Cookie(Config.CURRENT_USERNAME, sessionUser.getUsername() + "_" + sessionUser.getId());
        //cookie 保存7天
        cookie.setMaxAge(60 * 60 * 24 * 7);
        response.addCookie(cookie);
    }

    /**
     * 注销时删除cookie信息
     *
     * @param request
     * @param response
     */
    private void delCookieUser(HttpServletRequest request, HttpServletResponse response,HttpSession session) {
        User sessionUser = (User) session.getAttribute(Config.CURRENT_USERNAME);
        sessionUser.setPassword(null);
        Cookie cookie = new Cookie(Config.CURRENT_USERNAME, sessionUser.getUsername() + "_" + sessionUser.getId());
        cookie.setMaxAge(-1);
        response.addCookie(cookie);
    }

    public User getUserInfo(User user) {
        return userService.getUserInfo(user);
    }
}
