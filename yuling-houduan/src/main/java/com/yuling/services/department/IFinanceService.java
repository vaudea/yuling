package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface IFinanceService {
    PageInfo<Department> selectFinanceList(Department finance);

    int insertFinance(Department department);

    int updateFinance(Department department);

    int deleteFinance(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}