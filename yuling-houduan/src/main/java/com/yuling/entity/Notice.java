package com.yuling.entity;

import lombok.Data;

@Data
public class Notice {
    private Long id;
    private String noticeTitle;
    private String noticeContent;
    private String unitDepartment;
    private String noticeType;
    private Character status;
    private String createTime;
    //    分页数据
    //当前页数
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
}
