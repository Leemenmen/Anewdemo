package com.newdmsp.demo.service;

import com.newdmsp.demo.entity.Score;


/**
 * 学生报告得分，总分为
（3/5+4/5+5/10+6/10+7/10+8/30+9/30)/7
大于等于0.7代表考核通过(Score)表服务接口
 *
 * @author makejava
 * @since 2022-11-15 09:47:22
 */
public interface ScoreService {


    int addScore(Score score);

    int updateScore(Score score);

    Score getDetailScore(String recordId);
}
