package com.yuling.controller.notice;

import com.yuling.common.Result;
import com.yuling.entity.Notice;
import com.yuling.services.notice.INoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class noticeController {

    @Autowired
    private INoticeService noticeService;
    @GetMapping("/getNotice")
    public Result selectNoticeList(Notice notice) {
        System.out.println();
        return Result.success(noticeService.selectNoticeList(notice));
    }

    @GetMapping("/getNoticeById")
    public Result selectNoticeById(Long id) {
        return Result.success(noticeService.selectNoticeById(id));
    }
    @PostMapping("/insertNotice")
    public Result insertNotice(@RequestBody Notice notice) {
        noticeService.insertNotice(notice);
        return Result.success();
    }
    @DeleteMapping("/deleteNoticeByIds/{ids}")
    public Result deleteNoticeByIds(@PathVariable Long[] ids) {
        noticeService.deleteNoticeByIds(ids);
        return Result.success();
    }

    @PutMapping("/updateNotice")
    public Result updateNotice(@RequestBody  Notice notice) {
        System.out.println("notice:"+notice);
        noticeService.updateNotice(notice);
        return Result.success();
    }

    @PutMapping("/updateStatus/{id}")
    public Result updateNoticeStatus(@PathVariable Long id) {
        noticeService.updateNoticeStatus(id);
        return Result.success();
    }
}

