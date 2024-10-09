package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface ITechnicalService {
    PageInfo<Department> selectTechnicalList(Department technical);

    int insertTechnical(Department department);

    int updateTechnical(Department department);

    int deleteTechnical(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}