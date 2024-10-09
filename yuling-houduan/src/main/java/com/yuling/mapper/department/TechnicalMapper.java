package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface TechnicalMapper  {
    List<Department> selectTechnicalList(Department technical);

    int insertTechnical(Department department);

    int updateTechnical(Department department);

    int deleteTechnical(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}