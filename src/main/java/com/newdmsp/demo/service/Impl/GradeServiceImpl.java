package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.GradeMapper;

import com.newdmsp.demo.entity.Grade;
import com.newdmsp.demo.service.GradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GradeServiceImpl implements GradeService {

    @Resource
    GradeMapper gradeMapper;


    @Override
    public int addGrade(Grade grade) {
        return gradeMapper.addGrade(grade);
    }

    @Override
    public int addGradeExp(Integer expId, Integer gradeId) {
        return gradeMapper.addGradeExp(expId, gradeId);
    }


}
