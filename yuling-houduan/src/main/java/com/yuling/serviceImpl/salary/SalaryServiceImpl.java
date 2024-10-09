package com.yuling.serviceImpl.salary;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yuling.entity.Employee;
import com.yuling.entity.Salary;
import com.yuling.exception.CustomException;
import com.yuling.mapper.employee.EmployeeMapper;
import com.yuling.mapper.salary.SalaryMapper;
import com.yuling.services.salary.ISalaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class SalaryServiceImpl implements ISalaryService {
    @Autowired
    private SalaryMapper salaryMapper;
    @Autowired
    private EmployeeMapper employeeMapper;

    // 工号查询
    @Override
    public PageInfo<Salary> selectMyInSalary(Salary salary) {
        PageHelper.startPage(salary.getPageNum(), salary.getPageSize());
        List<Salary> list = salaryMapper.selectMyInSalary(salary);
        return PageInfo.of(list);
    }

    // 查询与模糊查询
    @Override
    public PageInfo<Salary> selectSalaryList(Salary salary) {
        // 开启分页逻辑
        PageHelper.startPage(salary.getPageNum(), salary.getPageSize());
        // 接下来的查询会自动按照当前开启的分页设置来查询
        List<Salary> list = salaryMapper.selectSalaryList(salary);
        return PageInfo.of(list);
    }

    // 添加数据
    @Override
    public int insertSalary(Salary salary) {
        Long jobNumber = salary.getJobNumber();
        Employee employee = employeeMapper.selectEmployeeByJobNumber(jobNumber);
        if (employee==null){
            throw new CustomException("工号 "+jobNumber+" 不存在，请确认工号是否正确");
        }
        salary.setBankCard(employee.getBankCard());
        salary.setTax(tax(salary));
        salary.setFiveInsurancesOneFund(fiveInsurancesAndOneHousingFund(salary));
        return salaryMapper.insertSalary(salary);
    }

    // 修改数据
    @Override
    public int updateSalary(Salary salary) {
        salary.setTax(tax(salary));
        salary.setFiveInsurancesOneFund(fiveInsurancesAndOneHousingFund(salary));
        return salaryMapper.updateSalary(salary);
    }

    // 删除单条数据
    @Override
    public int deleteSalaryById(Long id) {
        return salaryMapper.deleteSalaryById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @Override
    public int deleteSalaryByIds(Long[] ids) {
        return salaryMapper.deleteSalaryByIds(ids);
    }

    /**
     * @param salary
     * @return
     */
    @Override
    public List<Salary> export(Salary salary) {
        return salaryMapper.selectSalaryList(salary);
    }

    public BigDecimal tax(Salary salary) {
        BigDecimal salaryBeforeTax = beforeTaxes(salary);
        BigDecimal fiveInsurancesAndOneHousingFund = fiveInsurancesAndOneHousingFund(salary);
        BigDecimal taxableIncome = salaryBeforeTax.subtract(fiveInsurancesAndOneHousingFund).subtract(new BigDecimal("5000"));

        // 确保应纳税所得额不低于零
        if (taxableIncome.compareTo(BigDecimal.ZERO) < 0) {
            taxableIncome = BigDecimal.ZERO;
        }

        BigDecimal taxAmount = BigDecimal.ZERO;
        if (taxableIncome.compareTo(new BigDecimal("36000")) <= 0) {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.03"));
        } else if (taxableIncome.compareTo(new BigDecimal("144000")) <= 0) {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.1")).subtract(new BigDecimal("2520"));
        } else if (taxableIncome.compareTo(new BigDecimal("300000")) <= 0) {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.2")).subtract(new BigDecimal("16920"));
        } else if (taxableIncome.compareTo(new BigDecimal("420000")) <= 0) {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.25")).subtract(new BigDecimal("31920"));
        } else if (taxableIncome.compareTo(new BigDecimal("660000")) <= 0) {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.3")).subtract(new BigDecimal("52920"));
        } else if (taxableIncome.compareTo(new BigDecimal("960000")) <= 0) {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.35")).subtract(new BigDecimal("85920"));
        } else {
            taxAmount = taxableIncome.multiply(new BigDecimal("0.45")).subtract(new BigDecimal("181920"));
        }

        return taxAmount.setScale(2, RoundingMode.HALF_UP);
    }

    //    计算五险一金
    public BigDecimal fiveInsurancesAndOneHousingFund(Salary salary) {
        //税前收入
        BigDecimal salarys = beforeTaxes(salary);
        //养老保险
        BigDecimal pensionInsurance = salarys.multiply(new BigDecimal(0.08));
        //医疗保险
        BigDecimal medicalInsurance = salarys.multiply(new BigDecimal(0.02));
        //失业保险
        BigDecimal unemploymentInsurance = salarys.multiply(new BigDecimal(0.005));
        //住房公积金
        BigDecimal housingFund = salarys.multiply(new BigDecimal(0.07));
        return pensionInsurance.add(medicalInsurance).add(unemploymentInsurance).add(housingFund).setScale(2, RoundingMode.HALF_UP);
    }

    //计算税前收入
    public BigDecimal beforeTaxes(Salary salary) {
        return salary.getBasicSalary()
                .add(salary.getOvertimePay())
                .add(salary.getBonus())
                .add(salary.getCommission())
                .add(salary.getAllowance())
                .subtract(salary.getDeductions())
                .setScale(2, RoundingMode.HALF_UP);
    }
}
