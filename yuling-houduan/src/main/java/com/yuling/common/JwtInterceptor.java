package com.yuling.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.yuling.entity.Login;
import com.yuling.exception.CustomException;
import com.yuling.serviceImpl.login.LoginServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource//自动注入，不需要Spring的bean注入
    private LoginServiceImpl loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.从http请中的header中获取token
        String authHeader = request.getHeader("Authorization");
        String token = null;
        // 检查Authorization头部是否存在且以"Bearer "开始
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // 提取令牌（去掉前缀"Bearer "）
            token = authHeader.substring(7);
        }

        if (StrUtil.isBlank(token)) {
            // 请求头中没有就从请求参数中获取
            token = request.getParameter("token");
        }
        // 2。开始执行认证
        if (StrUtil.isBlank(token)) {
            throw new CustomException("无token，请重新登录");
        }
        // 获取token中的userId
        String userId;
        Login login;
        try {
            //开始执行jwt解码
            userId = JWT.decode(token).getAudience().get(0);
            // 根据token中的userid查询数据库
            login = loginService.selectEmployeeByJobNumber(Long.parseLong(userId));
            login.init();
        } catch (Exception e) {
            String errMsg = "token验证失败，请重新登录";
            log.error(errMsg + "token=" + token, e);
            throw new CustomException("token验证失败，请重新登录");
        }
        if (login == null) {
            throw new CustomException("用户不存在，请重新登录");
        }
        try {
            //用户密码加签验证token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(login.getPpd())).build();
            jwtVerifier.verify(token);//验证token
        } catch (Exception e) {
            throw new CustomException("token验证失败，请重新登录");
        }
        log.info("token验证成功，允许放行");
        return true;
    }
}
