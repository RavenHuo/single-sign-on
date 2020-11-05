package com.raven.sso.springboot.filter;

import com.raven.sso.springboot.common.Constant;
import com.raven.sso.springboot.common.UserInfoCache;
import com.raven.sso.springboot.common.exception.SsoException;
import com.raven.sso.springboot.common.utils.JwtUtil;
import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-04 10:19
 */
@Component
@Slf4j
public class RequestFilter implements Filter {

    /**
     * jwt token
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String uri = httpServletRequest.getRequestURI();
        // 此处可转化为db 或redis查询
        if (Constant.LOGIN.equals(uri)) {
            chain.doFilter(request, response);
        }
        else {
            Cookie[] cookies = httpServletRequest.getCookies();
            Cookie ssoToken = null;
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
                        chain.doFilter(request, response);
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
