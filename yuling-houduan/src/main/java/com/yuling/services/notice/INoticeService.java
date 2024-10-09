package com.yuling.services.notice;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Notice;

import java.util.List;

public interface INoticeService {
    public PageInfo<Notice> selectNoticeList(Notice notice);

    public int insertNotice(Notice notice);

    public int deleteNoticeByIds(Long[] ids);

    public int updateNotice(Notice notice);

    public Notice selectNoticeById(Long id);

    public int updateNoticeStatus(Long id);


}
