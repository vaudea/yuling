package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;

import java.util.List;

public interface ISalesService {
    PageInfo<Department> selectSalesList(Department Sales);

    int insertSales(Department department);

    int updateSales(Department department);

    int deleteSales(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}