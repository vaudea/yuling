package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.TechnicalImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/technical")
public class TechnicalController {

    @Autowired
    private TechnicalImpl technical;

    @GetMapping("/getList")
    public Result selectTechnicalList(Department humanResources) {
        PageInfo<Department> pageInfo = this.technical.selectTechnicalList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加技术部信息")
    public Result insertTechnical(@RequestBody Department department) {
        technical.insertTechnical(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改技术部信息")
    public Result updateTechnical(@RequestBody Department department) {
        technical.updateTechnical(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除技术部信息")
    public Result deleteTechnical(@PathVariable Long departmentId) {
        technical.deleteTechnical(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(technical.selectDepartmentPeople());
    }
}
