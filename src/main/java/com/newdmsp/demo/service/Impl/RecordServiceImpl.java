package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.RecordMapper;
import com.newdmsp.demo.entity.Record;
import com.newdmsp.demo.service.RecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class RecordServiceImpl implements RecordService {

    @Resource
    RecordMapper recordMapper;

    @Override
    public int addRecord(Record record) {
        return recordMapper.addRecord(record);
    }

    @Override
    public int upRecord(String id, String score) {
        return recordMapper.upRecord(id,score);
    }

    @Override
    public Record fingRecord(Integer expId, Integer id) {
        return recordMapper.findRecord(expId, id);
    }

    @Override
    public int submitRecord(Integer recordId) {
        return recordMapper.submitRecord(recordId);
    }

    @Override
    public int updateStuRecord(Record record) {
        return recordMapper.updateStuRecord(record);
    }

    @Override
    public int addDesc(Record record) {
        return recordMapper.addDesc(record);
    }

    @Override
    public Record getExpAndRecord(Integer expId, Integer sid) {
        return recordMapper.getExpAndRecord(expId,sid);
    }

    @Override
    public boolean dynamicUpTable(Map<String, Object> upDesMap, Integer id) {
        int upNum = recordMapper.dynamicUpTable(upDesMap,id);
        if (upNum > 0) {

            return true;
        } else {
            return false;
        }
    }

    @Override
    public int submitStepRecord(Integer recordId) {
        return recordMapper.submitStepRecord(recordId);
    }

    @Override
    public int updateStepRecord(Record record) {
        return recordMapper.updateStepRecord(record);
    }

    @Override
    public int addDescSet(String dataDesc, String dataDescVal, Integer id) {
        return recordMapper.addDescSet(dataDesc, dataDescVal,id);
    }

    @Override
    public int submitRecordTime(Integer recordId, String format) {
        return recordMapper.submitRecordTime(recordId, format);
    }

}
