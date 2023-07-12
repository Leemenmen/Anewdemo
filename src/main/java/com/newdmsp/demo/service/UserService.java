package com.newdmsp.demo.service;

import com.newdmsp.demo.entity.Record;
import com.newdmsp.demo.entity.Tgrade;
import com.newdmsp.demo.entity.User;
import com.newdmsp.demo.utils.PageModel;

import java.util.List;
import java.util.Map;

public interface UserService {

    boolean userIsExisted(User user);

    User getUserInfo(User user);
    User getUserInfo1(String username);
    User getTeachUserInfo(Integer id);

    int delTgrade(Integer id);

    int addGtoe(Tgrade tgrade);

    int getNumGtoe(Integer expid);

    List<Tgrade> getGrades(Integer id);

    List<Tgrade> getGtoeTgrades(Integer expid,Integer tid);

    List<User> getUsers(User user);

    int updateUser(User user);

    int addUser(User user);

    Tgrade getStuGrade(Integer id);

    int updateStu(User user);

    List<Record> getGtoe(Record record);

    List<User> getUsersByGradePage(PageModel model);

    int getUsersByGradePageTotals(PageModel model);

    int banStu(User user);
}
