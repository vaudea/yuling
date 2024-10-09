package com.yuling.serviceImpl.menu;

import com.yuling.entity.Employee;
import com.yuling.entity.Menu;
import com.yuling.mapper.menu.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yuling.services.menu.IMenuService;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuServiceImpl implements IMenuService {


    @Autowired
    private MenuMapper menuMapper;
    /**
     * @param id
     * @return
     */
    @Override
    public int deleteMenuById(Long id) {
        return menuMapper.deleteMenuById(id);
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public int insertMenu(Menu menu) {
        menu.setParentId(parentMenu(menu.getParentId()));
        return menuMapper.insertMenu(menu);
    }

    /**
     * @param menu
     * @return
     */
    @Override
    public int updateMenu(Menu menu) {
        menu.setParentId(parentMenu(menu.getParentId()));
        return menuMapper.updateMenu(menu);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public Menu selectMenuById(Long id) {
        return menuMapper.selectMenuById(id);
    }

    /**
     * @return
     */
    @Override
    public List<Menu> selectAllMenus() {
        return menuMapper.selectAllMenus();
    }
    @Override
    public List<Menu> buildMenuTree() {
        List<Menu> allMenus = menuMapper.selectAllMenus();
        return buildTree(allMenus, null);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public List<Menu> selectParentMenu(Long id) {
        return menuMapper.selectParentMenu(id);
    }

    /**
     * @return
     */
    @Override
    public List<Menu> selectPermissionMenus(Employee employee) {
        List<Menu> menus = menuMapper.selectPermissionMenus(employee);
        return buildTree(menus, null);

    }

    private List<Menu> buildTree(List<Menu> allMenus, Long parentId) {
        List<Menu> menuTree = new ArrayList<>();
        for (Menu menu : allMenus) {
            if ((parentId == null && menu.getParentId() == null) || (menu.getParentId() != null && menu.getParentId().equals(parentId))) {
                menu.setChildren(buildTree(allMenus, menu.getId()));
                menuTree.add(menu);
            }
        }
        return menuTree;
    }

    private Long parentMenu(Long parentId){
        if (parentId == 0){
            parentId = null;
        }else{
            Menu menu = menuMapper.selectMenuById(parentId);
            if(!menu.getSubmenu()){
                menu.setSubmenu(true);
                menuMapper.updateMenu(menu);
            }
        }
        return parentId;
    }
}
