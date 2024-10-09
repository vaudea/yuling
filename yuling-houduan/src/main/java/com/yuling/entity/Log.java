package com.yuling.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Excel(name = "员工工号",width = 20)
    private Long jobNumber;
    @Excel(name = "员工姓名")
    private String name;
    @Excel(name = "操作")
    private String logName;
    //操作时间，获取的是当前的unix时间戳 单位 秒
    @Excel(name = "操作时间", format = "yyyy-MM-dd HH:mm:ss", width = 20)
    private Long logTime;
    @Excel(name = "用户IP",width = 20)
    private String userIp;
    @Excel(name = "用户所在地",width = 20)
    private String userAddress;
    //用户端口
    @Excel(name = "用户端口")
    private Integer userPort;
    @Excel(name = "用户主机名",width = 20)
    private String userHost;
    // 分页数据
    // 当前页数
    private Integer pageNum;
    // 每页条数
    private Integer pageSize;

    private String remark;



    public Log(Long jobNumber, String logName, Long logTime, String userIp,String userAddress ,Integer userPort, String userHost, String remark) {
        this.jobNumber = jobNumber;
        this.logName = logName;
        this.logTime = logTime;
        this.userIp = userIp;
        this.userAddress = userAddress;
        this.userPort = userPort;
        this.userHost = userHost;
        this.remark = remark;
    }
    public Log() {}
}
