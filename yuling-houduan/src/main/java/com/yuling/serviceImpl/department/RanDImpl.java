package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.RandDMapper;
import com.yuling.services.department.IRandDService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RanDImpl implements IRandDService {

    @Autowired
    private RandDMapper randDMapper;
    /**
     * @param randD
     * @return
     */
    @Override
    public PageInfo<Department> selectRandDList(Department randD) {
        PageHelper.startPage(randD.getPageNum(), randD.getPageSize());
        List<Department> list = randDMapper.selectRandDList(randD);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertRandD(Department department) {
        return randDMapper.insertRandD(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateRandD(Department department) {
        return randDMapper.updateRandD(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteRandD(Long departmentId) {
        return randDMapper.deleteRandD(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return randDMapper.selectDepartmentPeople();
    }
}
