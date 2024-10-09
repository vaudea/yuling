package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.Result;
import com.yuling.common.AutoLog;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.SalesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/sales")
public class SalesController {

    @Autowired
    private SalesImpl sales;

    @GetMapping("/getList")
    public Result selectSalesList(Department humanResources) {
        PageInfo<Department> pageInfo = this.sales.selectSalesList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加销售部信息")
    public Result insertSales(@RequestBody Department department) {
        sales.insertSales(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改销售部信息")
    public Result updateSales(@RequestBody Department department) {
        sales.updateSales(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除销售部信息")
    public Result deleteSales(@PathVariable Long departmentId) {
        sales.deleteSales(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(sales.selectDepartmentPeople());
    }
}
