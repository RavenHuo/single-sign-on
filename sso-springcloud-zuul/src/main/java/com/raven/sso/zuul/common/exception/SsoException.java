package com.raven.sso.zuul.common.exception;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-04 11:55
 */
public class SsoException extends RuntimeException{

    private static final long serialVersionUID = -5632037901248135197L;

    /**
     * code 错误码
     */
    private String code;

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

    /**
     * msg 错误信息
     */
    private String msg;

    public SsoException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
