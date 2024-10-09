package com.yuling.serviceImpl.log;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Log;
import com.yuling.mapper.log.LogMapper;
import com.yuling.services.log.IlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class logServiceImpl implements IlogService {
    @Autowired
    private LogMapper logMapper;

    /**
     * @param log
     * @return
     */
    @Override
    public int insertLog(Log log) {
        return logMapper.insertLog(log);
    }

    /**
     * @param ids
     * @return
     */
    @Override
    public int deleteLogByIds(Long[] ids) {
        return logMapper.deleteLogByIds(ids);
    }

    /**
     * @param log
     * @return
     */
    @Override
    public PageInfo<Log> selectLogList(Log log) {
        PageHelper.startPage(log.getPageNum(), log.getPageSize());
        List<Log> logs = logMapper.selectLogList(log);
        return PageInfo.of(logs);
    }

    /**
     * @param log
     * @return
     */
    @Override
    public List<Log> exportLog(Log log) {
        return logMapper.selectLogList(log);
    }
}
