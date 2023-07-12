package com.newdmsp.demo.service;

import com.newdmsp.demo.entity.Record;

import java.util.Map;

public interface RecordService {

    int addRecord(Record record);

    int upRecord(String id, String score);

    Record fingRecord(Integer expId, Integer id);

    int submitRecord(Integer recordId);

    int updateStuRecord(Record record);

    int addDesc(Record record);

    Record getExpAndRecord(Integer expId, Integer sid);

    boolean dynamicUpTable(Map<String, Object> upDesMap, Integer id);

    int submitStepRecord(Integer recordId);

    int updateStepRecord(Record record);

    int addDescSet(String dataDesc, String dataDescVal, Integer id);

    int submitRecordTime(Integer recordId, String format);
}
