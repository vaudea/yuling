package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SalesMapper  {
    List<Department> selectSalesList(Department Sales);

    int insertSales(Department department);

    int updateSales(Department department);

    int deleteSales(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}