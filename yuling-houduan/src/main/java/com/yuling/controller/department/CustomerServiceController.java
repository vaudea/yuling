package com.yuling.controller.department;

import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Department;
import com.yuling.serviceImpl.department.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/department/customerService")
public class CustomerServiceController {

    @Autowired
    private CustomerServiceImpl customerService;

    @GetMapping("/getList")
    public Result selectCustomerServiceList(Department humanResources) {
        PageInfo<Department> pageInfo = this.customerService.selectCustomerServiceList(humanResources);
        return Result.success(pageInfo);
    }

    @PostMapping("/insertDepartment")
    @AutoLog("添加客服部信息")
    public Result insertCustomerService(@RequestBody Department department) {
        customerService.insertCustomerService(department);
        return Result.success();
    }

    @PutMapping("/updateDepartment")
    @AutoLog("修改客服部信息")
    public Result updateCustomerService(@RequestBody Department department) {
        customerService.updateCustomerService(department);
        return Result.success();
    }

    @DeleteMapping("/deleteDepartment/{departmentId}")
    @AutoLog("删除客服部信息")
    public Result deleteCustomerService(@PathVariable Long departmentId) {
        customerService.deleteCustomerService(departmentId);
        return Result.success();
    }

    //查询部门高级职员
    @GetMapping("/getPeople")
    public Result selectDepartmentPeople() {
        return Result.success(customerService.selectDepartmentPeople());
    }
}
