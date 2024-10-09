package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface AdministrationMapper {
    List<Department> selectAdministrationList(Department administration);

    int insertAdministration(Department department);

    int updateAdministration(Department department);

    int deleteAdministration(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}
