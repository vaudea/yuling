package com.yuling.common;

import org.springframework.stereotype.Component;

@Component
public class Result {

    private static final String SUCCESS = "200";
    private static final String ERROR = "400";

//    前端返回的状态码
    private String code;
//    报错信息返回前台
    private String msg;
//数据形式
    private Object data;
    //表示请求成功 -> 适用于增删改
    public static Result success(){
        Result result = new Result();
        result.setCode(SUCCESS);
        return result;
    }
    //请求成功给前端返回数据 -> 适用于查询
    public static Result success(Object data){
        Result result = new Result();
        result.setCode(SUCCESS);
        result.setData(data);
        return result;
    }
    //请求失败
    public static Result error(String msg){
        Result result = new Result();
        result.setCode(ERROR);
        result.setMsg(msg);
        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
