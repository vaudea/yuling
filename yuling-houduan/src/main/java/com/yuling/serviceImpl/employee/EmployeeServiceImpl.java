package com.yuling.serviceImpl.employee;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.entity.Position;
import com.yuling.exception.CustomException;
import com.yuling.mapper.employee.EmployeeMapper;
import com.yuling.services.employee.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeServiceImpl implements IEmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    //工号查询
    @Override
    public Employee selectEmployeeByJobNumber(Long jobNumber) {
        return employeeMapper.selectEmployeeByJobNumber(jobNumber);
    }

    //查询与模糊查询
    @Override
    public PageInfo<Employee> selectEmployeeList(Employee employee) {
        // 开启分页逻辑
        // import com.github.pagehelper.PageHelper;
        PageHelper.startPage(employee.getPageNum(), employee.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Employee> list = employeeMapper.selectEmployeeList(employee);
        return PageInfo.of(list);
    }

    // 添加数据
    @Override
    public int insertEmployee(Employee employee) {
        //判空
        validateEmployeeFields(employee);
        //判端重复
        checkForDuplicateDetails(employee, true);
        employee.setCreateTime(new Date());
        employee.setUpdateTime(new Date());
        employee.setJobNumber(generateJobNumber(employee));
        if(employeeMapper.selectEmployeeByJobNumber(employee.getJobNumber())!=null){
            throw new CustomException("工号重复，请重新输入");
        }
        if (employee.getPassword() == null) employee.setPassword("123456");

        return employeeMapper.insertEmployee(employee);
    }

    // 修改数据
    @Override
    public int updateEmployee(Employee employee) {
        validateEmployeeFields(employee);
        checkForDuplicateDetails(employee, false);
        employee.setUpdateTime(new Date());
        return employeeMapper.updateEmployee(employee);
    }


    /**
     * 修改员工头像
     *
     * @param employee
     * @return
     */
    @Override
    public int updateAvatar(Employee employee) {
        return employeeMapper.updateAvatar(employee);
    }

    /**
     * 更改员工状态
     *
     * @param jobNumber
     * @return
     */
    @Override
    public int updateEmployeeStatus(Long jobNumber) {
        return employeeMapper.updateEmployeeStatus(jobNumber);
    }

    //删除单条数据
    @Override
    public int deleteEmployeeByJobNumber(Long jobNumber) {
        return employeeMapper.deleteEmployeeByJobNumber(jobNumber);
    }

    /**
     * 导出员工数据
     *
     * @param employee
     * @return
     */
    @Override
    public List<Employee> export(Employee employee) {
        return employeeMapper.selectEmployeeList(employee);
    }

    //批量删除数据
    @Override
    public int deleteEmployeeByJobNumbers(Long[] jobNumbers) {
        return employeeMapper.deleteEmployeeByJobNumbers(jobNumbers);
    }

    @Override
    public List<Department> departmentList(Long fatherId) {
        return employeeMapper.departmentList(fatherId);
    }

    @Override
    public List<Position> positionList(Long pid) {
        return employeeMapper.positionList(pid);
    }

    @Override
    public Long createNumber() {
        return employeeMapper.createNumber() + 1;
    }

    /**
     * @param
     * @return
     */
    @Override
    public  boolean existsPhone(String phone) {
        return employeeMapper.existsPhone(phone);
    }

    /**
     * @param
     * @return
     */
    @Override
    public boolean existsEmail(String email) {
        return employeeMapper.existsEmail(email);
    }

    /**
     * @param
     * @return
     */
    @Override
    public boolean existsBankCard(String bankCard) {
        return employeeMapper.existsBankCard(bankCard);
    }

    //生成工号
    public Long generateJobNumber(Employee employee) {
        String createTime = new SimpleDateFormat("yyyyMMdd").format(employee.getCreateTime());
        String createNumber = String.format("%03d", createNumber());
        return Long.parseLong(createTime + createNumber);
    }

    //对已有数据的检查
    private void checkForDuplicateDetails(Employee employee, boolean isNew) {
        // 获取当前数据库中的员工记录，只在更新操作时获取
        Employee existingEmployee = isNew ? null : employeeMapper.selectEmployeeByJobNumber(employee.getJobNumber());

        // 检查电话号码
        if (isNew || (existingEmployee != null && !employee.getPhone().equals(existingEmployee.getPhone()))) {
            if (employeeMapper.existsPhone(employee.getPhone())) {
                throw new CustomException(employee.getName() + " 的电话 " + employee.getPhone() + " 已存在，请重新输入");
            }
        }

        // 检查电子邮箱
        if (isNew || (existingEmployee != null && !employee.getEmail().equals(existingEmployee.getEmail()))) {
            if (employeeMapper.existsEmail(employee.getEmail())) {
                throw new CustomException(employee.getName() + " 的邮箱 " + employee.getEmail() + " 已存在，请重新输入");
            }
        }

        // 检查银行卡号
        if (isNew || (existingEmployee != null && !employee.getBankCard().equals(existingEmployee.getBankCard()))) {
            if (employeeMapper.existsBankCard(employee.getBankCard())) {
                throw new CustomException(employee.getName() + " 的银行卡 " + employee.getBankCard() + " 已存在，请重新输入");
            }
        }
    }

    //对空值的检查
    private void validateEmployeeFields(Employee employee) {
        if (StrUtil.isEmpty(employee.getName())) throw new CustomException("员工姓名不能为空！");
        if (employee.getGender() == null) throw new CustomException("员工性别不能为空！");
        if (StrUtil.isEmpty(employee.getPhone())) throw new CustomException("员工电话不能为空！");
        if (StrUtil.isEmpty(employee.getEmail())) throw new CustomException("员工邮箱不能为空！");
        if (StrUtil.isEmpty(employee.getBankCard())) throw new CustomException("员工银行卡号不能为空！");
        if (employee.getDepartmentId() == null) throw new CustomException("所属部门不能为空！");
        if (employee.getPositionId() == null) throw new CustomException("职位信息不能为空！");

    }

}
