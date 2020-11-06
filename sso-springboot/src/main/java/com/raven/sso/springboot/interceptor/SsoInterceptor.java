package com.raven.sso.springboot.interceptor;

import com.raven.sso.springboot.common.Constant;
import com.raven.sso.springboot.common.UserInfoCache;
import com.raven.sso.springboot.common.exception.SsoException;
import com.raven.sso.springboot.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-06 20:34
 */
@Component
@Slf4j
public class SsoInterceptor implements HandlerInterceptor {

    /**
     * jwt token
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String uri = request.getRequestURI();
        // 此处可转化为db 或redis查询
        if (Constant.LOGIN.equals(uri) || !uri.startsWith(Constant.API)) {
            return true;
        }
        else {
            Cookie[] cookies = request.getCookies();
            Cookie ssoToken = null;
            if (cookies == null) {
                log.info("ssoToken is null");
                throw new SsoException("999999","please login");
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
                throw new SsoException("999999","please login");
            } else {
                String ssoTokenValue = ssoToken.getValue();
                Map<String, Object> ssoMap = JwtUtil.parseToken(ssoTokenValue, jwtSecret);
                // 判断ssoToken中有无解析出userId
                if (null != ssoMap && ssoMap.size() != 0 && ssoMap.containsKey(Constant.SSO_TOKEN_KEY)) {
                    // 判断 可以换成db 或redis
                    // 获取用户id
                    String userId = ssoMap.get(Constant.SSO_TOKEN_KEY).toString();
                    if (StringUtils.isEmpty(userId)) {
                        log.info("ssoToken error ");
                        throw new SsoException("999998", "ssoToken parse error");
                    }
                    // 判断缓存中是否存在userId
                    UserInfoCache userInfoCache = UserInfoCache.getInstance();
                    if (userInfoCache.ifExist(userId)) {
                        return true;
                    }
                    else {
                        log.info("ssoToken error ");
                        throw new SsoException("999998", "ssoToken parse error");
                    }
                } else {
                    log.info("ssoToken error ");
                    throw new SsoException("999998", "ssoToken parse error");
                }
            }
        }
    }
}
