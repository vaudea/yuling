package com.yuling.controller.login;

import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import com.yuling.common.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin
public class CaptchaController {

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping("/captcha")
    public void captcha(@RequestParam String key, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //指定验证码的宽高和字符个数
        SpecCaptcha captcha = new SpecCaptcha(130, 55, 5);
        //设置验证码类型  这里是数字加字母
        captcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);
        // 存储验证码到 Redis，设置过期时间，如 5 分钟
        redisUtil.setUtil("captcha:" + key, captcha.text().toLowerCase(), 5, TimeUnit.MINUTES);

        request.getSession().setAttribute("captcha", captcha.text().toLowerCase());
        captcha.out(response.getOutputStream());

    }

    public static void setHeader(HttpServletResponse response) {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0L);
    }
}