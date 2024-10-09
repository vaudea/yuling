package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.HumanResourcesImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/humanResources")
public class HumanResourcesController {

    @Autowired
    private HumanResourcesImpl humanResources;

    @GetMapping("/getList")

    public Result selectHrList(Department humanResources) {
        PageInfo<Department> pageInfo = this.humanResources.selectHrList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加人事部门信息")
    public Result insertHR(@RequestBody Department department) {
        humanResources.insertHR(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改人事部门信息")
    public Result updateHR(@RequestBody Department department) {
        humanResources.updateHR(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除人事部门信息")
    public Result deleteHR(@PathVariable Long departmentId) {
        humanResources.deleteHR(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(humanResources.selectDepartmentPeople());
    }
}
