package com.yuling.controller.menu;

import com.yuling.common.Result;
import com.yuling.entity.Employee;
import com.yuling.entity.Menu;
import com.yuling.services.employee.IEmployeeService;
import com.yuling.services.menu.IMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@Slf4j
public class menuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IEmployeeService employeeService;
    /**
     * 用户获取菜单和路由
     */
    @GetMapping("/getMenu")
    public Result getMenu(Long jobNumber) {
        System.out.println("工号是："+jobNumber);
        Employee employee = employeeService.selectEmployeeByJobNumber(jobNumber);
        System.out.println(employee);
        List<Menu> menus = null;
        if (employee.getPermissionLevel() == 5){
            menus = menuService.buildMenuTree();
        }else{
            menus = menuService.selectPermissionMenus(employee);
        }
//        List<Menu> menus = menuService.buildMenuTree();

//        menus.forEach(System.out::println);
        ResponseEntity<List<Menu>> ok = ResponseEntity.ok(menus);
        return Result.success(ok);
    }

    @GetMapping("/getMenus")
    public Result getMenus(Long jobNumber) {
        System.out.println("工号是："+jobNumber);
        Employee employee = employeeService.selectEmployeeByJobNumber(jobNumber);
        System.out.println(employee);
        List<Menu> menus = menus = menuService.buildMenuTree();
        ResponseEntity<List<Menu>> ok = ResponseEntity.ok(menus);
        return Result.success(ok);
    }

    @GetMapping("/getMenuById")
    public Result getMenuById(Long id) {
        return Result.success(menuService.selectMenuById(id));
    }

    //获取父菜单
    @GetMapping("/ParentMenu")
    public Result getParentMenu(Long id) {
        return Result.success(menuService.selectParentMenu(id));
    }

    @PostMapping("/insertMenu")
    public Result insertMenu(@RequestBody Menu menu) {
        return Result.success(menuService.insertMenu(menu));
    }
    @PutMapping("/updateMenu")
    public Result updateMenu(@RequestBody Menu menu) {
        return Result.success(menuService.updateMenu(menu));
    }
    @DeleteMapping("/deleteMenuById/{id}")
    public Result deleteMenuById(@PathVariable Long id) {
        System.out.println(id);
        return Result.success(menuService.deleteMenuById(id));
    }
}
