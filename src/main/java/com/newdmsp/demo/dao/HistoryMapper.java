package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.History;
import com.newdmsp.demo.utils.PageModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HistoryMapper {

    List<History> getHistory(Integer id);

    List<History> getHistoryByPage(PageModel model);

    int getHistoryByTotals(PageModel model);
}
