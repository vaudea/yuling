package com.yuling.mapper.menu;

import com.yuling.entity.Employee;
import com.yuling.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MenuMapper {
    public int deleteMenuById(Long id);
    public int insertMenu(Menu menu);
    public int updateMenu(Menu menu);
    public Menu selectMenuById(Long id);
    public List<Menu> selectAllMenus();
    public List<Menu> selectParentMenu(Long id);
    public  List<Menu> selectPermissionMenus(Employee employee);

}
