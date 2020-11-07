package com.raven.sso.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.raven.sso.zuul.common.Constant;
import com.raven.sso.zuul.common.ResponseResult;
import com.raven.sso.zuul.common.UserInfoCache;
import com.raven.sso.zuul.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-07 13:08
 */
@Order(-1)
@Slf4j
public class SsoZuulFilter extends ZuulFilter {

    @PostConstruct
    public void init(){
        log.info("SsoZuulFilter  init-----------");
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();

        // 此处可转化为db 或redis查询
        if (Constant.LOGIN.equals(uri)) {
            return null;
        }
        Cookie[] cookies = request.getCookies();
        Cookie ssoToken = null;
        if (cookies == null) {
            log.info("ssoToken is null");
            zuulResponse(503,"998","please login");
            return null;
        }
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (Constant.SSO_COOKIE_NAME.equals(name)) {
                ssoToken = cookie;
            }
        }

        // 判断cookie
        if (null == ssoToken) {
            log.info("ssoToken is null");
            zuulResponse(503,"998","please login");
            return null;
        }

        String ssoTokenValue = ssoToken.getValue();
        Map<String, Object> ssoMap = JwtUtil.parseToken(ssoTokenValue, jwtSecret);
        // 判断ssoToken中有无解析出userId
        if (null != ssoMap && ssoMap.size() != 0 && ssoMap.containsKey(Constant.SSO_TOKEN_KEY)) {
            // 判断 可以换成db 或redis
            // 获取用户id
            String userId = ssoMap.get(Constant.SSO_TOKEN_KEY).toString();
            if (StringUtils.isEmpty(userId)) {
                log.info("ssoToken error ");
                zuulResponse(503,"997","ssoToken parse error");
                return null;
            }
            // 判断缓存中是否存在userId
            UserInfoCache userInfoCache = UserInfoCache.getInstance();
            if (!userInfoCache.ifExist(userId)) {
                log.info("ssoToken error ");
                zuulResponse(503,"997","ssoToken parse error");
                return null;
            }
            return null;
        } else {
            log.info("ssoToken error ");
            zuulResponse(503,"997","ssoToken parse error");
            return null;
        }
    }

    private void zuulResponse(Integer status, String code, String msg) {
        RequestContext currentContext = RequestContext.getCurrentContext();
        //false  不会继续往下执行 不会调用服务接口了 网关直接响应给客户了
        currentContext.setSendZuulResponse(false);
        ResponseResult responseEntity = ResponseResult.fail(code, msg);
        currentContext.setResponseBody(JSONObject.toJSONString(responseEntity));
        currentContext.setResponseStatusCode(status);

    }
}
