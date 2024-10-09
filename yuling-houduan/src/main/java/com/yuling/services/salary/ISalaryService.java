package com.yuling.services.salary;

import com.yuling.entity.Employee;
import com.yuling.entity.Salary;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ISalaryService {
    /**
     * 查询工资信息
     *
     * @return 工资信息
     */
    public PageInfo<Salary> selectMyInSalary(Salary salary);

    /**
     * 查询工资信息列表
     *
     * @param salary 工资信息查询条件
     * @return 工资信息集合
     */
    public PageInfo<Salary> selectSalaryList(Salary salary);

    /**
     * 新增工资信息
     *
     * @param salary 工资信息
     * @return 结果
     */
    public int insertSalary(Salary salary);

    /**
     * 修改工资信息
     *
     * @param salary 工资信息
     * @return 结果
     */
    public int updateSalary(Salary salary);

    /**
     * 删除工资信息
     *
     * @param id
     * @return 结果
     */
    public int deleteSalaryById(Long id);

    /**
     * 批量删除工资记录
     *
     */
    int deleteSalaryByIds(Long[] ids);


    List<Salary> export(Salary salary);
}
