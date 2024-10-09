package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.RanDImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/randD")
public class RandDController {

    @Autowired
    private RanDImpl ranD;

    @GetMapping("/getList")
    public Result selectRandDList(Department humanResources) {
        PageInfo<Department> pageInfo = this.ranD.selectRandDList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加研发部信息")
    public Result insertRandD(@RequestBody Department department) {
        ranD.insertRandD(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改研发部信息")
    public Result updateRandD(@RequestBody Department department) {
        ranD.updateRandD(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除研发部信息")
    public Result deleteRandD(@PathVariable Long departmentId) {
        ranD.deleteRandD(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(ranD.selectDepartmentPeople());
    }
}
