package com.yuling.entity;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private Long id;
    private Long parentId;
    private String title;
    private String icon;
    private String router;
    private Boolean submenu;
    private List<Menu> children;
}
