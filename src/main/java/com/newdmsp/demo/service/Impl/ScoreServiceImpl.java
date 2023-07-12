package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.entity.Score;
import com.newdmsp.demo.dao.ScoreMapper;
import com.newdmsp.demo.service.ScoreService;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * 学生报告得分，总分为
（3/5+4/5+5/10+6/10+7/10+8/30+9/30)/7
大于等于0.7代表考核通过(Score)表服务实现类
 *
 * @author makejava
 * @since 2022-11-15 09:47:22
 */
@Service
public class ScoreServiceImpl implements ScoreService {
    @Resource
    ScoreMapper scoreMapper;


    @Override
    public int addScore(Score score) {
        return scoreMapper.addScore(score);
    }

    @Override
    public int updateScore(Score score) {
        return scoreMapper.updateScore(score);
    }

    @Override
    public Score getDetailScore(String recordId) {
        return scoreMapper.getDetailScore(recordId);
    }
}
