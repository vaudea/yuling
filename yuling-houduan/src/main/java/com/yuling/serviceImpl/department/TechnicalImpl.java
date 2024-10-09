package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.TechnicalMapper;
import com.yuling.services.department.ITechnicalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TechnicalImpl implements ITechnicalService {

    @Autowired
    private TechnicalMapper technicalMapper;

    /**
     * @param technical
     * @return
     */
    @Override
    public PageInfo<Department> selectTechnicalList(Department technical) {
        PageHelper.startPage(technical.getPageNum(), technical.getPageSize());
        List<Department> list = technicalMapper.selectTechnicalList(technical);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertTechnical(Department department) {
        return technicalMapper.insertTechnical(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateTechnical(Department department) {
        return technicalMapper.updateTechnical(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteTechnical(Long departmentId) {
        return technicalMapper.deleteTechnical(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return technicalMapper.selectDepartmentPeople();
    }
}
