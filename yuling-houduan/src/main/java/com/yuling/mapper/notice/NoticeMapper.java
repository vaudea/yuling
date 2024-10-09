package com.yuling.mapper.notice;

import com.yuling.entity.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NoticeMapper {

    public List<Notice> selectNoticeList(Notice notice);

    public int insertNotice(Notice notice);

    public int deleteNoticeByIds(Long[] ids);

    public int updateNotice(Notice notice);

    public Notice selectNoticeById(Long id);

    public int updateNoticeStatus(Long id);
}

