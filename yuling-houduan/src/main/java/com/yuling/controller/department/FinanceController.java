package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.FinanceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/finance")
public class FinanceController {

    @Autowired
    private FinanceImpl finance;

    @GetMapping("/getList")
    public Result selectFinanceList(Department humanResources) {
        PageInfo<Department> pageInfo = this.finance.selectFinanceList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加财务部门信息")
    public Result insertFinance(@RequestBody Department department) {
        finance.insertFinance(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改财务部门信息")
    public Result updateFinance(@RequestBody Department department) {
        finance.updateFinance(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除财务部门信息")
    public Result deleteFinance(@PathVariable Long departmentId) {
        finance.deleteFinance(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(finance.selectDepartmentPeople());
    }
}
