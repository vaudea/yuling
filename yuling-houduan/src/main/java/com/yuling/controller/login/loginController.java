package com.yuling.controller.login;

import com.yuling.common.AutoLog;
import com.yuling.common.Result;
import com.yuling.entity.Login;
import com.yuling.exception.CustomException;
import com.yuling.services.log.IlogService;
import com.yuling.services.login.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class loginController {

    @Autowired
    private ILoginService loginService;
    @Autowired
    private IlogService logService;

    @PostMapping("/login")
    @AutoLog("登录")
    public Result login(@RequestBody Login login,@RequestParam String key ) {
        if (login==null){
            throw new CustomException("用户信息为空");
        }
        Login user = loginService.loginEmployee(login,key);
        return Result.success(user);
    }
    @PutMapping("/resetPassword")
    @AutoLog("重置密码")
    public Result resetPassword(@RequestBody Login login) {
        loginService.resetPassword(login);
        return Result.success();
    }

}
