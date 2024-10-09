package com.yuling.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Salary {
    private Long id;
    @Excel(name = "员工工号")
    private Long jobNumber;
    @Excel(name = "员工姓名")
    private String name;
    @Excel(name = "发放日期",format = "yyyy-MM-dd",width = 20)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    @Excel(name = "基本工资")
    private BigDecimal basicSalary;
    @Excel(name = "加班工资")
    private BigDecimal overtimePay;
    @Excel(name = "奖金")
    private BigDecimal bonus;
    @Excel(name = "提成")
    private BigDecimal commission;
    @Excel(name = "津贴")
    private BigDecimal allowance;
    @Excel(name = "扣款")
    private BigDecimal deductions;
    @Excel(name = "税前工资")
    private BigDecimal tax;
    @Excel(name = "五险一金")
    private BigDecimal fiveInsurancesOneFund;
    @Excel(name = "税后工资")
    private BigDecimal netSalary;
    @Excel(name = "支付方式")
    private String paymentMethod;
    @Excel(name = "扣款备注")
    private String remarks;
    @Excel(name = "银行卡号", width = 25) // 设置宽度为25
    private String bankCard;

    private String queryPayDate;
    //    分页数据
    //当前页数
    private Integer pageNum;
    //每页条数
    private Integer pageSize;
}
