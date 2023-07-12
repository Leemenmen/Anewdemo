package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.QtoaMapper;
import com.newdmsp.demo.entity.Qtoa;
import com.newdmsp.demo.service.QtoaService;
import com.newdmsp.demo.utils.Result;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class QtoaServiceImpl implements QtoaService {

    @Resource
    QtoaMapper qtoaMapper;

    @Override
    public List<Qtoa> getQtoa(Qtoa qtoa) {
        return qtoaMapper.getQtoa(qtoa);
    }

    @Override
    public int addQtoa(Qtoa qtoa) {
        return qtoaMapper.addQtoa(qtoa);
    }

    @Override
    public List<Qtoa> getQtoaTeach(Qtoa qtoa) {
        return qtoaMapper.getQtoaTeach(qtoa);
    }

    @Override
    public int update(Qtoa qtoa) {
        return qtoaMapper.updateQtoa(qtoa);
    }


}
