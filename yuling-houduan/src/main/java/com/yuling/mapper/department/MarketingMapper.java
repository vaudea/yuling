package com.yuling.mapper.department;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface MarketingMapper  {
    List<Department> selectMarketingList(Department marketing);

    int insertMarketing(Department department);

    int updateMarketing(Department department);

    int deleteMarketing(Long departmentId);

    //查询部门高级职员
    List<Employee> selectDepartmentPeople();
}