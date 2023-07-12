package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.ExpMapper;
import com.newdmsp.demo.entity.Exp;
import com.newdmsp.demo.entity.History;
import com.newdmsp.demo.service.ExpService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExpServiceImpl implements ExpService {
    @Resource
    ExpMapper expMapper;

    @Override
    public Exp getExpsByExpId(Integer expId) {
        return expMapper.getExpsByExpId(expId);
    }

    @Override
    public List<Exp> getExps(Exp exp) {
        return expMapper.getExps(exp);
    }

    @Override
    public int addExp(Exp exp) {
        return expMapper.addExp(exp);
    }

    @Override
    public int addHistory(History history) {
        return expMapper.addHistory(history);
    }

    @Override
    public List<Exp> getGtoe() {
        return expMapper.getGtoe();
    }

    @Override
    public List<Exp> getExpsByGradeId(Integer gradeid) {
        return expMapper.getExpsByGradeId(gradeid);
    }

    @Override
    public List<Exp> getTeachGradeExp(Integer id) {
        return expMapper.getTeachGradeExp(id);
    }

    @Override
    public List<Exp> getExpDataSets(Integer expId) {
        return expMapper.getExpDataSets(expId);
    }

    @Override
    public int addExpStep(Exp exp) {
        return expMapper.addExpStep(exp);
    }
}
