package com.yuling.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class Department {
    private Long departmentId;
    @Excel(name = "部门名称")
    private String departmentName;
    @Excel(name = "部门经理ID")
    private String managerId;

    private String fatherId;
    @Excel(name = "部门经理名称")
    private String managerName;
    @Excel(name = "部门人数")
    private Long count;
    // 分页数据
    // 当前页数
    private Integer pageNum;
    // 每页条数
    private Integer pageSize;
}
