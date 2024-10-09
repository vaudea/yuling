package com.yuling.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class Position {
    private Long positionId;
    @Excel(name = "职位")
    private String positionName;

    private Long permissionLevel;
}
