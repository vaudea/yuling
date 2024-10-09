package com.yuling.mapper.salary;

import com.yuling.entity.Employee;
import com.yuling.entity.Salary;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalaryMapper {

    /**
     * 查询工资列表
     *
     * @param salary 查询条件
     * @return 工资列表
     */
    List<Salary> selectSalaryList(Salary salary);

    /**
     * 通过员工工号查询单条工资信息
     *
     * @return 工资信息
     */
    List<Salary> selectMyInSalary(Salary salary);

    /**
     * 插入新的工资记录
     *
     * @param salary 工资信息
     */
    int insertSalary(Salary salary);

    /**
     * 更新现有工资记录
     *
     * @param salary 工资信息
     */
    int updateSalary(Salary salary);

    /**
     * 删除单条工资记录
     *
     */
    int deleteSalaryById(Long id);

    /**
     * 批量删除工资记录
     *
     */
    int deleteSalaryByIds(Long[] ids);


}
