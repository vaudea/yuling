package com.yuling.mapper.employee;

import com.yuling.entity.Department;
import com.yuling.entity.Employee;
import com.yuling.entity.Position;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface EmployeeMapper {
    /**
     * 查询员工管理
     *
     * @param jobNumber 员工管理主键
     * @return 员工管理
     */
    public Employee selectEmployeeByJobNumber(Long jobNumber);

    /**
     * 查询员工管理列表
     *
     * @param employee 员工管理
     * @return 员工管理集合
     */
    public List<Employee> selectEmployeeList(Employee employee);

    /**
     * 新增员工管理
     *
     * @param employee 员工管理
     * @return 结果
     */
    public int insertEmployee(Employee employee);

    /**
     * 修改员工管理
     *
     * @param employee 员工管理
     * @return 结果
     */
    public int updateEmployee(Employee employee);
    /**
     * 修改员工头像
     * */
    public int updateAvatar(Employee employee);
    /**
     * 更改员工状态
     */
    public int updateEmployeeStatus(Long jobNumber);


    /**
     * 删除员工管理
     *
     * @param jobNumber 员工管理主键
     * @return 结果
     */
    public int deleteEmployeeByJobNumber(Long jobNumber);

    /**
     * 批量删除员工管理
     *
     * @param jobNumbers 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteEmployeeByJobNumbers(Long[] jobNumbers);

    //部门下拉框
    public List<Department> departmentList(Long fatherId);

    //职位下拉框
    public List<Position> positionList(Long pid);

    @Select("select count(*) from employee where DATE(create_time) = CURDATE()")
    public Long createNumber();

    //    重复数据判断
     boolean existsPhone(String phone);

    boolean existsEmail(String email);

    boolean existsBankCard(String bankCard);
}
