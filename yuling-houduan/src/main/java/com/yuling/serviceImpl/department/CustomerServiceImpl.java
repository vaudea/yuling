package com.yuling.serviceImpl.department;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.mapper.department.CustomerServiceMapper;
import com.yuling.services.department.ICustomerServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerServiceImpl implements ICustomerServiceService {

    @Autowired
    private CustomerServiceMapper customerServiceMapper;
    /**
     * @param customerService
     * @return
     */
    @Override
    public PageInfo<Department> selectCustomerServiceList(Department customerService) {
        PageHelper.startPage(customerService.getPageNum(), customerService.getPageSize());
        List<Department> list = customerServiceMapper.selectCustomerServiceList(customerService);
        return PageInfo.of(list);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int insertCustomerService(Department department) {
        return customerServiceMapper.insertCustomerService(department);
    }

    /**
     * @param department
     * @return
     */
    @Override
    public int updateCustomerService(Department department) {
        return customerServiceMapper.updateCustomerService(department);
    }

    /**
     * @param departmentId
     * @return
     */
    @Override
    public int deleteCustomerService(Long departmentId) {
        return customerServiceMapper.deleteCustomerService(departmentId);
    }

    /**
     * @return
     */
    @Override
    public List<Employee> selectDepartmentPeople() {
        return customerServiceMapper.selectDepartmentPeople();
    }
}
