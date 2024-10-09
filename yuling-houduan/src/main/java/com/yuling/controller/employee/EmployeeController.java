package com.yuling.controller.employee;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Employee;
import com.yuling.exception.CustomException;
import com.yuling.services.employee.IEmployeeService;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private IEmployeeService employeeService;


    //查询员工
    @GetMapping("/employeeList")
    public Result list(Employee employee) {
        Employee user = employeeService.selectEmployeeByJobNumber(employee.getUserJobNumber());
        if (!(user.getPositionId() >= 3 || user.getFatherId() == 1)) {
            throw new CustomException("您没有权限查看员工信息");
        }
        // 限制只能查询出自己部门和同等职位的数据 --- 除人力资源部职位
        if (user.getFatherId() != 1 && user.getPositionId() < 5) {
            //这里的 employee.setDepartmentId 其实是 父部门id
            employee.setDepartmentId(user.getFatherId());
            //pid是自己的id 限制查询高于自己职位的
            employee.setUserPositionId(user.getPositionId());
        }
        PageInfo<Employee> list = employeeService.selectEmployeeList(employee);
        log.info("员工数据查询成功");
        return Result.success(list);
    }

    //添加员工
    @PostMapping("/employeeInsert")
    @AutoLog("添加员工信息")
    public Result insertEmployee(@RequestBody Employee employee) {
        Employee user = employeeService.selectEmployeeByJobNumber(employee.getUserJobNumber());
        if (employee.getPositionId() > user.getPositionId() && employee.getUserJobNumber() != user.getJobNumber() && user.getPositionId() < 5) {
            if (user.getFatherId() != employee.getFatherId() && user.getFatherId()!=1)
                throw new CustomException("您不能添加其他部门的成员信息");
            else throw new CustomException("您不能添加同等职位或更高职位的员工信息");
        }
        employeeService.insertEmployee(employee);
        return Result.success();

    }

    //修改员工信息
    @PutMapping("/employeeUpdate")
    @AutoLog("修改员工信息")
    public Result updateEmployee(@RequestBody Employee employee) {
        Employee user = employeeService.selectEmployeeByJobNumber(employee.getUserJobNumber());
        if (employee.getUserJobNumber() != user.getJobNumber() && employee.getPositionId() > user.getPositionId() &&  user.getPositionId() < 5) {
            if (user.getFatherId() != employee.getFatherId() && user.getFatherId()!=1)
                throw new CustomException("您不能修改其他部门的成员信息");
            else throw new CustomException("您不能修改同等职位或更高职位的员工信息");
        }
        employeeService.updateEmployee(employee);
        return Result.success();
    }

    //修改员工状态
    @PutMapping("/employeeStatus/{jobNumber}")
    @AutoLog("修改员工状态")
    public Result updateStatus(@PathVariable Long jobNumber) {
        if (jobNumber == 0) {
            throw new CustomException("管理员账户不能停用！！！");
        }
        employeeService.updateEmployeeStatus(jobNumber);

        return Result.success();
    }

    //修改头像
    @PutMapping("/updateAvatar")
    public Result updateAvatar(@RequestBody Employee employee) {
        employeeService.updateAvatar(employee);
        return Result.success();
    }

    //根据工号获取头像
    @GetMapping("/getAvatar")
    public Result selectAvatarByJobNumber(Long jobNumber) {
        Employee employee = employeeService.selectEmployeeByJobNumber(jobNumber);
        if (employee == null) {
            throw new CustomException("该员工不存在");
        }
        return Result.success(employee.getAvatar());
    }

    //根据工号获取员工信息
    @GetMapping("/getEmployee")
    public Result selectEmployeeByJobNumber(Long jobNumber) {
        Employee employee = employeeService.selectEmployeeByJobNumber(jobNumber);
        if (employee == null) {
            throw new CustomException("该员工不存在");
        }
        return Result.success(employee);
    }

    //员工删除
    @DeleteMapping("/employeeDelete/{jobNumbers}")
    @AutoLog("删除员工信息")
    public Result deleteEmployee(@PathVariable Long[] jobNumbers) {
        employeeService.deleteEmployeeByJobNumbers(jobNumbers);
        return Result.success();
    }

    //导出数据
    @GetMapping("/export")
    @AutoLog("导出员工信息")
    public Result export(HttpServletResponse response, Employee employee) throws IOException {
        List<Employee> export = employeeService.export(employee);
        if (export == null) {
            throw new CustomException("当前无数据，请联系管理员进行处理！");
        }
        // 创建工作簿和表格参数
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("员工数据", "员工表"), Employee.class, export);
        // 设置响应输出的头信息
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("员工数据.xlsx", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        // 将Excel写入到响应输出流
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        workbook.close();

        return Result.success();
    }

    //导入数据
    @PostMapping("/upload")
    @AutoLog("导入员工信息")
    public Result upload(@RequestParam("file") MultipartFile upload) throws Exception {
        if (upload.isEmpty()) {
            throw new CustomException("上传文件为空");
        }
        // 1.解析文档需要用到ImportParams对象
        final ImportParams importParams = new ImportParams();
        //设置标题的行数，有标题时一定要有
        importParams.setTitleRows(1);
        //设置表头的行数
        importParams.setHeadRows(1);

        // 2.使用ExcelImportUtil工具类获取Excel表格中的内容，并转为List<T>，实际情况下我们需要知道上传的表格是什么类型的
        // *.因为所有的pojo都是按照用户需求创建的，拥有模板，上传的数据必须符合这些模板
        // *.那么实际开发中，可以通过访问的url判断本次上传的文件是属于什么类型的pojo
        final List<Employee> employees = ExcelImportUtil.importExcel(
                upload.getInputStream(),
                Employee.class,
                importParams
        );
        // 循环插入每个员工
        for (Employee employee : employees) {
            employeeService.insertEmployee(employee);
        }
        return Result.success();
    }

    //部门下拉框查询
    @GetMapping("/department")
    public Result departmentList(Long fatherId) {
        return Result.success(employeeService.departmentList(fatherId));
    }

    //职位下拉框查询
    @GetMapping("/position")
    public Result positionList(Long positionId) {
        return Result.success(employeeService.positionList(positionId));
    }
}
