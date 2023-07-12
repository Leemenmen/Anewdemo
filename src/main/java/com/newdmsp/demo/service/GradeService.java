package com.newdmsp.demo.service;


import com.newdmsp.demo.entity.Grade;


public interface GradeService {


    int addGrade(Grade grade);

    int addGradeExp(Integer expId, Integer gradeId);
}
