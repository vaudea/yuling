package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.FinanceMapper;
import com.yuling.services.department.IFinanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class FinanceImpl implements IFinanceService {
    @Autowired
    private FinanceMapper financeMapper;
    /**
     * @param finance
     * @return
     */
    @Override
    public PageInfo<Department> selectFinanceList(Department finance) {
        PageHelper.startPage(finance.getPageNum(), finance.getPageSize());
        List<Department> list = financeMapper.selectFinanceList(finance);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertFinance(Department department) {
        return financeMapper.insertFinance(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateFinance(Department department) {
        return financeMapper.updateFinance(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteFinance(Long departmentId) {
        return financeMapper.deleteFinance(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return financeMapper.selectDepartmentPeople();
    }
}
