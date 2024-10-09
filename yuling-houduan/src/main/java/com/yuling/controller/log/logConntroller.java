package com.yuling.controller.log;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.yuling.common.Result;
import com.yuling.entity.Log;
import com.yuling.services.log.IlogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/log")
public class logConntroller {
    @Autowired
    private IlogService logService;

    @GetMapping("/selectLogList")
    public Result selectLogList(Log log) {
        return Result.success(logService.selectLogList(log));
    }
    //导出日志信息
    @GetMapping("/exportLog")
    public void export(HttpServletResponse response, Log log) throws IOException {
        List<Log> export = logService.exportLog(log);

        // 创建工作簿和表格参数
        Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("操作日志", "日志表"), Log.class, export);
        // 设置响应输出的头信息
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("操作日志.xlsx", "UTF-8"));
        response.setCharacterEncoding("UTF-8");
        // 将Excel写入到响应输出流
        workbook.write(response.getOutputStream());
        response.getOutputStream().flush();
        workbook.close();

    }
    @PostMapping("/insertLog")
    public Result insertLog(@RequestBody Log log, HttpServletRequest request) {
        log.setUserIp(request.getRemoteAddr());
        log.setUserHost(request.getLocalName());
        log.setUserPort(request.getLocalPort());
        log.setLogTime(System.currentTimeMillis()/1000);
        logService.insertLog(log);
        return Result.success();
    }
    
    @DeleteMapping("/deleteLog/{ids}")
    public Result deleteLogByIds(@PathVariable Long[] ids) {
        return Result.success(logService.deleteLogByIds(ids));
    }

}
