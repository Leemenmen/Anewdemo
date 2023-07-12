package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Score;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 学生报告得分，总分为
（3/5+4/5+5/10+6/10+7/10+8/30+9/30)/7
大于等于0.7代表考核通过(Score)表数据库访问层
 *
 * @author makejava
 * @since 2022-11-15 09:47:22
 */
 @Mapper
public interface ScoreMapper {


    int addScore(Score score);

    int updateScore(Score score);

    Score getDetailScore(String recordId);
}

