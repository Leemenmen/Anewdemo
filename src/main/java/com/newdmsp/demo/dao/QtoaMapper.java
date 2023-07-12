package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Qtoa;
import com.newdmsp.demo.utils.Result;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface QtoaMapper {
    @Select("select * from qtoa where sid = #{sid}")
    List<Qtoa> getQtoa(Qtoa qtoa);

    int addQtoa(Qtoa qtoa);

    List<Qtoa> getQtoaTeach(Qtoa qtoa);

    int updateQtoa(Qtoa qtoa);
}
