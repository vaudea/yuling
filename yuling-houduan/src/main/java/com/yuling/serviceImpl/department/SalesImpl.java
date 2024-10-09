package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.SalesMapper;
import com.yuling.services.department.ISalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SalesImpl implements ISalesService {

    @Autowired
    private SalesMapper salesMapper;

    /**
     * @param sales
     * @return
     */
    @Override
    public PageInfo<Department> selectSalesList(Department sales) {
        PageHelper.startPage(sales.getPageNum(), sales.getPageSize());
        List<Department> list =salesMapper.selectSalesList(sales);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertSales(Department department) {
        return salesMapper.insertSales(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateSales(Department department) {
        return salesMapper.updateSales(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteSales(Long departmentId) {
        return salesMapper.deleteSales(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return salesMapper.selectDepartmentPeople();
    }
}
