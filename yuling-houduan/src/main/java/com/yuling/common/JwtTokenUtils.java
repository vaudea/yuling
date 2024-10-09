package com.yuling.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.yuling.entity.Login;
import com.yuling.serviceImpl.login.LoginServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;


@Component
public class JwtTokenUtils {
    // 登录的Service
    private static LoginServiceImpl staticLoginService;
    // 日志
    private static final Logger log = LoggerFactory.getLogger(JwtTokenUtils.class);

    @Resource
    private LoginServiceImpl loginService;

    //spring容器启动的时候执行该方法,类似vue2的created:{}
    @PostConstruct
    public void setUserService() {
        staticLoginService = loginService;
    }

    /**
     * 生成token
     * @param userId 员工工号
     * @param sign 密码
     * @return token
     */
    public static String getToken(String userId, String sign) {
        return JWT.create().withAudience(userId) //将userId保存到token， 作为载荷
                .withExpiresAt(DateUtil.offsetHour(new Date(), 2)) //2小时后token过期
                .sign(Algorithm.HMAC256(sign));
    }


    /**
     * @return 获取当前用户具体信息
     */
    public static Login getCurrentUser() {
        String token = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String authHeader = request.getHeader("Authorization");
            // 检查Authorization头部是否存在且以"Bearer "开始
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                // 提取令牌（去掉前缀"Bearer "）
                token = authHeader.substring(7);
            }

            if (StrUtil.isBlank(token)) {
                // 请求头中没有就从请求参数中获取
                token = request.getParameter("token");
            }
            if (StrUtil.isNotBlank(token)){
                log.error("获取当前登录的token失败, token:{}",token);
            }
            //解析token，获取用户id
            String userId = JWT.decode(token).getAudience().get(0);
            return staticLoginService.selectEmployeeByJobNumber(Long.valueOf(userId));
        }catch (Exception e){
            log.error("获取当前登录的用户信息失败, token:{}",token);
            return null;
        }
    }
}
