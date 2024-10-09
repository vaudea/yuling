package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.AdministrationMapper;
import com.yuling.services.department.IAdministrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministrationImpl implements IAdministrationService {

    @Autowired
    AdministrationMapper administrationMapper;

    /**
     * @param administration
     * @return
     */
    @Override
    public PageInfo<Department> selectAdministrationList(Department administration) {
        PageHelper.startPage(administration.getPageNum(), administration.getPageSize());
        List<Department> list = administrationMapper.selectAdministrationList(administration);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertAdministration(Department department) {
        return administrationMapper.insertAdministration(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateAdministration(Department department) {
        return administrationMapper.updateAdministration(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteAdministration(Long departmentId) {
        return administrationMapper.deleteAdministration(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return administrationMapper.selectDepartmentPeople();
    }
}
