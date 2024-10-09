package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface CustomerServiceMapper  {
    List<Department> selectCustomerServiceList(Department customerService);

    int insertCustomerService(Department department);

    int updateCustomerService(Department department);

    int deleteCustomerService(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}