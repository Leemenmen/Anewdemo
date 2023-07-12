package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Record;
import com.newdmsp.demo.entity.Tgrade;
import com.newdmsp.demo.entity.User;
import com.newdmsp.demo.utils.PageModel;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    int UserExisted(User user);

    User getUserInfo(User user);

    @Select("select * from user where username = #{username}")
    User getUserInfo1(String username);

    User getTeachUserInfo(Integer id);

    @Delete("delete from gtoe where id = #{id}")
    int delTgrades(Integer id);

    int addTgrade(Tgrade tgrade);

    int getNumGtoe(Integer expid);

    @Select("select g.id gradeid,g.gname from grade g where tid = #{id}")
    List<Tgrade> getGrades(Integer id);

    List<Tgrade> getGtoeTgrades(Integer expid,Integer tid);

    List<User> getUsers(User user);

    int updateUser(User user);

    int addUser(User user);

    @Select("select * from grade where id = #{id}")
    Tgrade getStuGrade(Integer id);

    @Update("update user set password=#{password} where id=#{id}")
    int updateStu(User user);

    List<Record> getGtoe(Record record);

    List<User> getUsersByGradePage(PageModel model);

    int getUsersByGradePageTotals(PageModel model);

    int banStu(User user);
}