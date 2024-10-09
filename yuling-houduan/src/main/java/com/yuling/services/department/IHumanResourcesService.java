package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface IHumanResourcesService {
    PageInfo<Department> selectHrList(Department humanResources);

    int insertHR(Department department);

    int updateHR(Department department);

    int deleteHR(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}
