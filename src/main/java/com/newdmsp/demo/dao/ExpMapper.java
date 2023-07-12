package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Exp;
import com.newdmsp.demo.entity.History;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;
@Mapper
public interface ExpMapper {
    Exp getExpsByExpId(Integer expId);

    List<Exp> getExps(Exp exp);

    int addExp(Exp exp);

    int addHistory(History history);

    List<Exp> getGtoe();

    List<Exp> getExpsByGradeId(Integer gradeid);

    List<Exp> getTeachGradeExp(Integer id);

    List<Exp> getExpDataSets(Integer expId);

    int addExpStep(Exp exp);
}
