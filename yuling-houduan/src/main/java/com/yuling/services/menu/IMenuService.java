package com.yuling.services.menu;

import com.yuling.entity.Employee;
import com.yuling.entity.Menu;

import java.util.List;

public interface IMenuService {
    public int deleteMenuById(Long id);
    public int insertMenu(Menu menu);
    public int updateMenu(Menu menu);
    public Menu selectMenuById(Long id);
    public List<Menu> selectAllMenus();
    public  List<Menu> buildMenuTree();
    public List<Menu> selectParentMenu(Long id);
    public  List<Menu> selectPermissionMenus(Employee employee);

}
