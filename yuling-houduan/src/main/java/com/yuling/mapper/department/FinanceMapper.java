package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface FinanceMapper  {
    List<Department> selectFinanceList(Department finance);

    int insertFinance(Department department);

    int updateFinance(Department department);

    int deleteFinance(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}