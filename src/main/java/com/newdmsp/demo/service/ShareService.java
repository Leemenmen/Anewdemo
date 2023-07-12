package com.newdmsp.demo.service;

import com.newdmsp.demo.entity.Share;
import com.newdmsp.demo.utils.PageModel;

import java.util.List;

public interface ShareService {
    List<Share> getShareByFlag(Integer flag, String uploadDes);
    void updateShare(Integer id, Integer count);

    int addShare(Share share);

    List<Share> getTestShares(Integer flag, Integer id);

    int upCheckShare(Share share);

    List<Share> getShareByFlagPage(PageModel model);

    int getShareByFlagPageTotals(PageModel model);

    List<Share> getTestShareByFlagPage(PageModel model);

    int getTestShareByFlagPageTotals(PageModel model);

    List<Share> getShareBystuId(PageModel model);

    int getShareBystuIdTotals(PageModel model);
}
