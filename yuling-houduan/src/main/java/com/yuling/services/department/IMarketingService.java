package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface IMarketingService {
    PageInfo<Department> selectMarketingList(Department marketing);

    int insertMarketing(Department department);

    int updateMarketing(Department department);

    int deleteMarketing(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}