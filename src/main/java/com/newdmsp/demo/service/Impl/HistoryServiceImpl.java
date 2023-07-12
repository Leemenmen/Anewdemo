package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.HistoryMapper;
import com.newdmsp.demo.entity.History;
import com.newdmsp.demo.service.HistoryService;
import com.newdmsp.demo.utils.PageModel;
import com.newdmsp.demo.utils.Result;
import com.newdmsp.demo.utils.ResultUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Resource
    HistoryMapper historyMapper;

    @Override
    public List<History> getHistory(Integer id) {
        return historyMapper.getHistory(id);
    }

    @Override
    public List<History> getHistoryByPage(PageModel model) {
        return historyMapper.getHistoryByPage(model);
    }

    @Override
    public int getHistoryByTotals(PageModel model) {
        return historyMapper.getHistoryByTotals(model);
    }


}
