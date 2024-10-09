package com.yuling.exception;

import com.yuling.common.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理器，用于集中处理应用中抛出的各种异常。
 * 使用 @ControllerAdvice 注解，该类会被 Spring MVC 识别为一个全局异常处理类，
 * 适用于标注为 @RestController 或者 @Controller 的所有控制器类。
 *
 * @ControllerAdvice 注解的 basePackages 属性用于指定此异常处理器应用的包范围。
 */
@ControllerAdvice(basePackages = "com.yuling.controller")
public class GlobalExceptionHandler {

    /**
     * 日志记录器，用于记录异常信息。
     */
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理所有未被捕获的 Exception 类型异常。
     * 当应用程序中抛出任何 Exception 类或其子类的实例时，此方法将被调用。
     * @param e 异常对象
     * @return 包含错误信息的 Result 对象，用于向客户端返回错误响应。
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        // 记录异常的堆栈跟踪，便于后期问题定位
        logger.error("系统异常", e);

        // 返回一个包含异常消息的错误结果，以便前端能够解析并展示给用户
        return Result.error("系统异常 "+e.getMessage());
    }

    /**
     * 处理自定义异常 CustomException。
     * 当应用程序中抛出自定义异常 CustomException 时，此方法将被调用。
     * @param e CustomException 异常对象
     * @return 包含错误信息的 Result 对象，用于向客户端返回错误响应。
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public Result customerError(CustomException e) {
        // 返回一个包含自定义异常信息的错误结果
        return Result.error(e.getMsg());
    }
}