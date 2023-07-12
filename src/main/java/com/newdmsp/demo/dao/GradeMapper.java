package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Grade;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface GradeMapper {


    int addGrade(Grade grade);

    int addGradeExp(Integer expId, Integer gradeId);
}
