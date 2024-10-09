package com.yuling.common;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yuling.entity.Log;
import com.yuling.entity.Login;
import com.yuling.services.log.IlogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
/**
 * 处理切面的监控
 */

@Component
@Aspect
public class LogAspect {
    @Autowired
    private IlogService logService;

    //环绕模式，至始至终环绕着书写了@AutoLog注解的接口
    @Around("@annotation(autoLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, AutoLog autoLog) throws Throwable {
        // 依旧为顺序结构执行代码 ， 当执行了书写了  @AutoLog  注解的接口 ，会首先进入该方法 ， 顺着该方法的顺序结构执行
        //使用 autoLog.value() 获取注解的值 例如 @AutoLog("操作日志") 获取 “操作日志”
        String logName = autoLog.value();
        //获取操作时间 unix时间戳 单位 秒
        long logTime = System.currentTimeMillis() / 1000;
        // 获取当前操作人信息
        Long userJobNumber = null;
        Login user = JwtTokenUtils.getCurrentUser();
        if(ObjectUtil.isNotNull(user)){
            userJobNumber = user.getJobNumber();
        }
        //操作人网络信息
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String userIp = IpAddressUtil.getIpAddress(request);
        //获取用户主机名
        String userHost = request.getHeader("X-Forwarded-For");
        if (StrUtil.isEmpty(userHost)){
            userHost = userIp;
        }
        //获取用户IP归属地
        String userAddress = IpAddressUtil.getCityInfoByMemorySearch(userIp);
        //获取用户端口
        Integer userPort = request.getRemotePort();
        // 表示为开始执行书写了  @AutoLog  注解的接口
        Result result = (Result) joinPoint.proceed();

        Object data = result.getData();
        if (data instanceof Login){
            Login login = (Login) data;
            if(userJobNumber == null){
                userJobNumber = login.getJobNumber();
                String remark = "登录成功";
            }
        }

        //当执行完写了  @AutoLog  注解的接口后，会执行往后的内容
        Log log = new Log(userJobNumber, logName, logTime, userIp, userAddress, userPort, userHost,"");
        logService.insertLog(log);

        return result;
    }

}
