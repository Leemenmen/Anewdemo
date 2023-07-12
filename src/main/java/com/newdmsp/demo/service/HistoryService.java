package com.newdmsp.demo.service;

import com.newdmsp.demo.entity.History;
import com.newdmsp.demo.utils.PageModel;
import com.newdmsp.demo.utils.Result;

import java.util.List;

public interface HistoryService {

    List<History> getHistory(Integer id);


    List<History> getHistoryByPage(PageModel model);

    int getHistoryByTotals(PageModel model);
}
