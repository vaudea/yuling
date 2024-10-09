package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface RandDMapper  {
    List<Department> selectRandDList(Department randD);

    int insertRandD(Department department);

    int updateRandD(Department department);

    int deleteRandD(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}