package com.yuling.controller.salary;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.github.pagehelper.PageInfo;
import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Employee;
import com.yuling.entity.Salary;
import com.yuling.exception.CustomException;
import com.yuling.services.salary.ISalaryService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/salary")
public class salaryController {
    @Autowired
    private ISalaryService salaryService;

    // 查询工资列表
    @GetMapping("/salaryList")
    public Result list(Salary salary){
        PageInfo<Salary> list = salaryService.selectSalaryList(salary);
        return Result.success(list);
    }

    // 添加工资信息
    @PostMapping("/salaryInsert")
    @AutoLog("添加工资信息")
    public Result insertSalary(@RequestBody Salary salary){
        salaryService.insertSalary(salary);
        return Result.success();
    }

    // 修改工资信息
    @PutMapping("/salaryUpdate")
    @AutoLog("修改工资信息")
    public Result updateSalary(@RequestBody Salary salary){
        salaryService.updateSalary(salary);
        return Result.success();
    }

    // 根据工号获取工资信息
    @GetMapping("/getSalary")
    public Result selectMyInSalary(Salary salary){
        return Result.success(salaryService.selectMyInSalary(salary));
    }

    // 删除工资信息
    @DeleteMapping("/salaryDelete/{ids}")
    @AutoLog("删除工资信息")
    public Result deleteSalary(@PathVariable Long[] ids){
        salaryService.deleteSalaryByIds(ids);
        return Result.success();
    }

    //导入数据
    @PostMapping("/upload")
    @AutoLog("导入工资数据")
    public Result upload(@RequestParam("file") MultipartFile upload) throws Exception {
        if (upload.isEmpty()){
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
        final List<Salary> salarys = ExcelImportUtil.importExcel(
                upload.getInputStream(),
                Salary.class,
                importParams
        );
        // 循环插入每个员工
        for (Salary salary : salarys) {
            salaryService.insertSalary(salary);
        }
        return Result.success();
    }
    //导出数据
    @GetMapping("/export")
    @AutoLog("导出工资数据")
    public Result export(HttpServletResponse response, Salary salary) throws IOException {
        List<Salary> export = salaryService.export(salary);
        if (export == null) {
            throw new CustomException("当前无数据，请联系管理员进行处理！");
        }



        // 创建工作簿和表格参数
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("薪资数据", "薪资表"), Salary.class, export);
        // 设置响应输出的头信息
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("薪资数据.xlsx", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        // 将Excel写入到响应输出流
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        workbook.close();

        return Result.success();
    }
    
    
}
