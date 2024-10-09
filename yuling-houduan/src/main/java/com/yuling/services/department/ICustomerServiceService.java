package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface ICustomerServiceService {
    PageInfo<Department> selectCustomerServiceList(Department customerService);

    int insertCustomerService(Department department);

    int updateCustomerService(Department department);

    int deleteCustomerService(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}