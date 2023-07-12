package com.newdmsp.demo.service;


import com.newdmsp.demo.entity.DataSet;

import java.util.List;

public interface DataService {

    List<DataSet> getDefaultData(Integer sid);

    int uploadSelfData(DataSet dataSet);

    int addDataSet(DataSet dataSet);

    List<DataSet> dataSetPathByExpId(Integer expId);
}
