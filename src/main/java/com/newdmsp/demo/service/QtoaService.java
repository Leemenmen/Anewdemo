package com.newdmsp.demo.service;

import com.newdmsp.demo.entity.Qtoa;
import com.newdmsp.demo.utils.Result;

import java.util.List;

public interface QtoaService {
    List<Qtoa> getQtoa(Qtoa qtoa);
    int addQtoa(Qtoa qtoa);

    List<Qtoa> getQtoaTeach(Qtoa qtoa);

    int update(Qtoa qtoa);
}
