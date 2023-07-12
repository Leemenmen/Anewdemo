package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.UserMapper;
import com.newdmsp.demo.entity.Record;
import com.newdmsp.demo.entity.Tgrade;
import com.newdmsp.demo.entity.User;
import com.newdmsp.demo.service.UserService;
import com.newdmsp.demo.utils.PageModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserMapper userMapper;

    @Override
    public boolean userIsExisted(User user) {
       return userMapper.UserExisted(user) > 0;
    }

    @Override
    public User getUserInfo(User user) {
        return userMapper.getUserInfo(user);
    }

    @Override
    public User getUserInfo1(String username) {
        return userMapper.getUserInfo1(username);
    }

    @Override
    public User getTeachUserInfo(Integer id) {
        return userMapper.getTeachUserInfo(id);
    }


    @Override
    public int delTgrade(Integer id) {
        return userMapper.delTgrades(id);
    }

    @Override
    public int addGtoe(Tgrade tgrade) {
        return userMapper.addTgrade(tgrade);
    }

    @Override
    public int getNumGtoe(Integer expid) {
        return userMapper.getNumGtoe(expid);
    }

    @Override
    public List<Tgrade> getGrades(Integer id) {
        return userMapper.getGrades(id);
    }

    @Override
    public List<Tgrade> getGtoeTgrades(Integer expid, Integer tid) {
        return userMapper.getGtoeTgrades(expid,tid);
    }

    @Override
    public List<User> getUsers(User user) {
        return userMapper.getUsers(user);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    @Override
    public int addUser(User user) {
        return userMapper.addUser(user);
    }

    @Override
    public Tgrade getStuGrade(Integer id) {
        return userMapper.getStuGrade(id);
    }

    @Override
    public int updateStu(User user) {
        return userMapper.updateStu(user);
    }

    @Override
    public List<Record> getGtoe(Record record) {
        return userMapper.getGtoe(record);
    }

    @Override
    public List<User> getUsersByGradePage(PageModel model) {
        return userMapper.getUsersByGradePage(model);
    }

    @Override
    public int getUsersByGradePageTotals(PageModel model) {
        return userMapper.getUsersByGradePageTotals(model);
    }

    @Override
    public int banStu(User user) {
        return userMapper.banStu(user);
    }


}
