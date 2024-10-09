package com.yuling.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class userInfo {

    private Long userPositionId;

    private Long userDepartmentId;

    private Long userDepartmentFatherId;

    private Long userJobNumber;
}
