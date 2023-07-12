package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Record;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

@Mapper
public interface RecordMapper {

    int addRecord(Record record);

    int upRecord(String id, String score);

//    @Select("select * from exprecord where expid=#{expid} and sid=#{sid}")
    Record findRecord(Integer expId, Integer id);


    int submitRecord(Integer recordId);

    int updateStuRecord(Record record);

    int addDesc(Record record);

    Record getExpAndRecord(Integer expId, Integer sid);

    int dynamicUpTable(@Param("upDesMap")Map<String, Object> upDesMap, @Param("id") Integer id);

    int submitStepRecord(Integer recordId);

    int updateStepRecord(Record record);

    int addDescSet(@Param("dataDesc") String dataDesc, @Param("dataDescVal") String dataDescVal, @Param("id") Integer id);

    int submitRecordTime(Integer recordId, String format);
}
