package com.yuling.services.log;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Log;

import java.util.List;

public interface IlogService {

    public int insertLog(Log log);

    public int deleteLogByIds(Long[] ids);

    public PageInfo<Log> selectLogList(Log log);

    public List<Log> exportLog(Log log);

}
