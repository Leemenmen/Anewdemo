package com.newdmsp.demo.service.Impl;

import com.newdmsp.demo.dao.ShareMapper;
import com.newdmsp.demo.entity.Share;
import com.newdmsp.demo.service.ShareService;
import com.newdmsp.demo.utils.PageModel;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ShareServiceImpl implements ShareService {
    @Resource
    ShareMapper shareMapper;


    @Override
    public List<Share> getShareByFlag(Integer flag, String uploadDes) {
        return shareMapper.getShareInfo(flag, uploadDes);
    }

    @Override
    public void updateShare(Integer id, Integer count) {
        shareMapper.updateShare(id,count);
    }

    @Override
    public int addShare(Share share) {
        return shareMapper.addShare(share);
    }

    @Override
    public List<Share> getTestShares(Integer flag, Integer id) {
        return shareMapper.getTestShares(flag,id);
    }

    @Override
    public int upCheckShare(Share share) {
        return shareMapper.upCheckShare(share);
    }

    @Override
    public List<Share> getShareByFlagPage(PageModel model) {
        return shareMapper.getShareByFlagPage(model);
    }

    @Override
    public int getShareByFlagPageTotals(PageModel model) {
        return shareMapper.getShareByFlagPageTotals(model);
    }

    @Override
    public List<Share> getTestShareByFlagPage(PageModel model) {
        return shareMapper.getTestShareByFlagPage(model);
    }

    @Override
    public int getTestShareByFlagPageTotals(PageModel model) {
        return shareMapper.getTestShareByFlagPageTotal(model);
    }

    @Override
    public List<Share> getShareBystuId(PageModel model) {
        return shareMapper.getShareBystuId(model);
    }

    @Override
    public int getShareBystuIdTotals(PageModel model) {
        return shareMapper.getShareBystuIdTotals(model);
    }


}
