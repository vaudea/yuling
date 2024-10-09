package com.yuling.controller;

import com.yuling.common.Result;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/test")
public class testController {

    @GetMapping
    public Result test(){
        return Result.success("测试成功");
    }
}
