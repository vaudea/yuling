package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.AdministrationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/administration")
public class AdministrationController {

    @Autowired
    private AdministrationImpl administration;

    @GetMapping("/getList")
    public Result selectAdministrationList(Department administration) {
        PageInfo<Department> pageInfo = this.administration.selectAdministrationList(administration);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加行政部信息")
    public Result insertAdministration(@RequestBody Department department) {
        administration.insertAdministration(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改行政部信息")
    public Result updateAdministration(@RequestBody Department department) {
        administration.updateAdministration(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除行政部信息")
    public Result deleteAdministration(@PathVariable Long departmentId) {
        administration.deleteAdministration(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(administration.selectDepartmentPeople());
    }
}
