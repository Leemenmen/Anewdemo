package com.newdmsp.demo.dao;

import com.newdmsp.demo.entity.Share;
import com.newdmsp.demo.utils.PageModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShareMapper {
    List<Share> getShareInfo(Integer flag, String uploadDes);
    void updateShare(Integer id,Integer count);

    int addShare(Share share);

    List<Share> getTestShares(Integer flag, Integer id);

    int upCheckShare(Share share);

    List<Share> getShareByFlagPage(PageModel model);

    int getShareByFlagPageTotals(PageModel model);

    List<Share> getTestShareByFlagPage(PageModel model);

    int getTestShareByFlagPageTotal(PageModel model);

    @Select("select * from share where user_id = #{data.userId} order by id limit #{beginIndex}, #{pageSize}")
    List<Share> getShareBystuId(PageModel model);

    @Select("select count(*) from share where user_id = #{data.userId}")
    int getShareBystuIdTotals(PageModel model);
}
