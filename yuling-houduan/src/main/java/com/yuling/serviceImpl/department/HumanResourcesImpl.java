package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.HumanResourcesMapper;
import com.yuling.services.department.IHumanResourcesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumanResourcesImpl implements IHumanResourcesService {


    @Autowired
    HumanResourcesMapper humanResourcesMapper;
    /**
     * @return
     */
    @Override
    public PageInfo<Department> selectHrList(Department humanResources) {
        PageHelper.startPage(humanResources.getPageNum(), humanResources.getPageSize());
        List<Department> list = humanResourcesMapper.selectHrList(humanResources);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertHR(Department department) {
        return humanResourcesMapper.insertHR(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateHR(Department department) {
        return humanResourcesMapper.updateHR(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteHR(Long departmentId) {
        return humanResourcesMapper.deleteHR(departmentId);
    }

    /**
     * 查询部门高级职员
     * @return  部门人员和工号
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return humanResourcesMapper.selectDepartmentPeople();
    }


}
