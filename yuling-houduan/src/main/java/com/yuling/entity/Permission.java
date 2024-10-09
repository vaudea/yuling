package com.yuling.entity;

import lombok.Data;

@Data
public class Permission {
    private Integer id;
    private Integer menuId;
    private Integer permissionLevel;
    private Integer departmentId;
    private String remarks;
}
