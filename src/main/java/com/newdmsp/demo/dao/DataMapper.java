package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.DataSet;
import com.newdmsp.demo.entity.Qtoa;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface DataMapper {

    List<DataSet> getDefaultData(Integer sid);

    int uploadSelfData(DataSet dataSet);

    int addDataSet(DataSet dataSet);

    List<DataSet> dataSetPathByExpId(Integer expId);
}
