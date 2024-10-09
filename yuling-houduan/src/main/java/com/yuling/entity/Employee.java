package com.yuling.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Table;
import java.util.Date;

@Data
public class Employee extends userInfo{
    private static final long serialVersionUID = 1L;

    private Long employeeId;

    @Excel(name = "员工工号", width = 20) // 设置宽度为15
    private Long jobNumber;

    @Excel(name = "员工姓名", width = 15) // 设置宽度为20
    private String name;

    @Excel(name = "性别", replace = {"男_0", "女_1"}, width = 5) // 设置宽度为10
    private String gender;

    @Excel(name = "出生日期", format = "yyyy-MM-dd", width = 15) // 设置宽度为20
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthdate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date hireDate;

    @Excel(name = "联系电话", width = 15) // 设置宽度为20
    private String phone;

    @Excel(name = "电子邮件", width = 30) // 设置宽度为30
    private String email;

    private String avatar;

    private Long departmentId;

    private Long positionId;

    @Excel(name = "创建时间", format = "yyyy-MM-dd HH:mm:ss", width = 20) // 设置宽度为25
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @Excel(name = "修改时间", format = "yyyy-MM-dd HH:mm:ss", width = 20) // 设置宽度为25
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    @Excel(name = "员工状态", replace = {"在职_0", "停职_1","离职_2"}, width = 10) // 设置宽度为10
    private int active;

    private String password;

    @Excel(name = "银行卡号", width = 25) // 设置宽度为25
    private String bankCard;

    @Excel(name = "所在部门", width = 15)
    private String departmentName;
    @Excel(name = "所处职位", width = 15)
    private String positionName;

    private Long fatherId;

    private Long pid;
    //职位权限
    private Long permissionLevel;

    // 分页数据
    // 当前页数
    private Integer pageNum;
    // 每页条数
    private Integer pageSize;
}
