package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.MarketingMapper;
import com.yuling.services.department.IMarketingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class MarketingImpl implements IMarketingService {

    @Autowired
    private MarketingMapper marketingMapper;
    /**
     * @param marketing
     * @return
     */
    @Override
    public PageInfo<Department> selectMarketingList(Department marketing) {
        PageHelper.startPage(marketing.getPageNum(), marketing.getPageSize());
        List<Department> list = marketingMapper.selectMarketingList(marketing);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertMarketing(Department department) {
        return marketingMapper.insertMarketing(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateMarketing(Department department) {
        return marketingMapper.updateMarketing(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteMarketing(Long departmentId) {
        return marketingMapper.deleteMarketing(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return marketingMapper.selectDepartmentPeople();
    }
}
