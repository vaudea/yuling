package com.yuling.serviceImpl.notice;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Notice;
import com.yuling.services.notice.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuling.mapper.notice.NoticeMapper;

import java.util.List;
@Service
public class NoticeImpl implements INoticeService {

    @Autowired
    private NoticeMapper noticeMapper;
    /**
     * @param notice
     * @return
     */
    @Override
    public PageInfo<Notice> selectNoticeList(Notice notice) {
        PageHelper.startPage(notice.getPageNum(),notice.getPageSize());
        List<Notice> list = noticeMapper.selectNoticeList(notice);
        return PageInfo.of(list);
    }


    /**
     * @param notice
     * @return
     */
    @Override
    public int insertNotice(Notice notice) {
        return noticeMapper.insertNotice(notice);
    }

    /**
     * @param ids
     * @return
     */
    @Override
    public int deleteNoticeByIds(Long[] ids) {
        return noticeMapper.deleteNoticeByIds(ids);
    }

    /**
     * @param notice
     * @return
     */
    @Override
    public int updateNotice(Notice notice) {
        return noticeMapper.updateNotice(notice);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Notice selectNoticeById(Long id) {
        return noticeMapper.selectNoticeById(id);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public int updateNoticeStatus(Long id) {
        return noticeMapper.updateNoticeStatus(id);
    }

}
