package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface IRandDService {
    PageInfo<Department> selectRandDList(Department randD);

    int insertRandD(Department department);

    int updateRandD(Department department);

    int deleteRandD(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}
