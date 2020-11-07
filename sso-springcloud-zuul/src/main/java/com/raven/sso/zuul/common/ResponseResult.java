package com.raven.sso.zuul.common;

import lombok.Builder;
import lombok.Data;

import java.security.interfaces.RSAKey;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-07 13:30
 */
@Data
public class ResponseResult<T> {
    private T obj;
    private String code;
    private String msg;

    private ResponseResult(){}

    public static ResponseResult fail(Object t) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode("9999");
        responseResult.setMsg("gateway auth fail");
        responseResult.setObj(t);
        return responseResult;
    }

    public static ResponseResult success(Object t) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode("0000");
        responseResult.setMsg("login success");
        responseResult.setObj(t);
        return responseResult;
    }

    public static ResponseResult fail(String code, String msg) {
        ResponseResult responseResult = new ResponseResult();
        responseResult.setCode(code);
        responseResult.setMsg(msg);
        return responseResult;
    }
}
