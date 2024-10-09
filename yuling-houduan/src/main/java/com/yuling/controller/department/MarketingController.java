package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.MarketingImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/marketing")
public class MarketingController {

    @Autowired
    private MarketingImpl marketing;

    @GetMapping("/getList")
    public Result selectMarketingList(Department humanResources) {
        PageInfo<Department> pageInfo = this.marketing.selectMarketingList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加市场部信息")
    public Result insertMarketing(@RequestBody Department department) {
        marketing.insertMarketing(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改市场部信息")
    public Result updateMarketing(@RequestBody Department department) {
        marketing.updateMarketing(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除市场部信息")
    public Result deleteMarketing(@PathVariable Long departmentId) {
        marketing.deleteMarketing(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(marketing.selectDepartmentPeople());
    }
}
