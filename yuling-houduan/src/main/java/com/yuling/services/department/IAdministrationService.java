package com.yuling.services.department;

import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface IAdministrationService {
    PageInfo<Department> selectAdministrationList(Department administration);

    int insertAdministration(Department department);

    int updateAdministration(Department department);

    int deleteAdministration(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}
