package com.yuling.mapper.log;

import com.yuling.entity.Log;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LogMapper {

    public int insertLog(Log log);

    public int deleteLogByIds(Long[] ids);

    public List<Log> selectLogList(Log log);
}
