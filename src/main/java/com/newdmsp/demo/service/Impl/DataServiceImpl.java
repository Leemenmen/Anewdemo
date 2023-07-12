package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.DataMapper;
import com.newdmsp.demo.dao.QtoaMapper;
import com.newdmsp.demo.entity.DataSet;
import com.newdmsp.demo.entity.Qtoa;
import com.newdmsp.demo.service.DataService;
import com.newdmsp.demo.service.QtoaService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataServiceImpl implements DataService {

    @Resource
    DataMapper dataMapper;


    @Override
    public List<DataSet> getDefaultData(Integer sid) {
        return dataMapper.getDefaultData(sid);
    }

    @Override
    public int uploadSelfData(DataSet dataSet) {
        return dataMapper.uploadSelfData(dataSet);
    }

    @Override
    public int addDataSet(DataSet dataSet) {
        return dataMapper.addDataSet(dataSet);
    }

    @Override
    public List<DataSet> dataSetPathByExpId(Integer expId) {
        return dataMapper.dataSetPathByExpId(expId);
    }

}
