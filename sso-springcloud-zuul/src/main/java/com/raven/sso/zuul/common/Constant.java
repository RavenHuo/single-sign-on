package com.raven.sso.zuul.common;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-05 21:29
 */
public class Constant {



    public static final String API = "/api";
    /**
     * 登录api
     */
    public static final String LOGIN = API + "/login";

    public static final String SSO = API + "/sso";

    /**
     * token map中的key
     */
    public static final String SSO_TOKEN_KEY = "userId";

    /**
     * cookie 的名称
     */
    public static final String SSO_COOKIE_NAME = "sso_cke";
}
