package com.raven.sso.springboot.controller;

import com.raven.sso.springboot.common.Constant;
import com.raven.sso.springboot.common.UserInfo;
import com.raven.sso.springboot.common.UserInfoCache;
import com.raven.sso.springboot.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-05 21:32
 */
@RestController
@Slf4j
public class LoginController {

    /**
     * jwt token
     */
    @Value("${jwt.secret}")
    private String jwtSecret;

    @GetMapping(Constant.LOGIN)
    public ResponseEntity<String> login(@RequestParam("name")String name, @RequestParam("phone")String phone, HttpServletResponse response) {
        UserInfo userInfo = UserInfo.builder()
                .name(name)
                .phone(phone)
                .build();
        Map<String, Object> ssoTokenMap = new HashMap<>(1);
        String userId = UUID.randomUUID().toString().replaceAll("-","");
        ssoTokenMap.put("userId", userId);
        String jwtToken = JwtUtil.generateJwtTokenByDays(ssoTokenMap, 1, jwtSecret);
        UserInfoCache userInfoCache =  UserInfoCache.getInstance();
        userInfoCache.put(userId, userInfo);
        Cookie cookie = new Cookie(Constant.SSO_COOKIE_NAME,jwtToken);
        response.addCookie(cookie);

        return ResponseEntity.ok("登录成功");
    }

    @GetMapping("/sso")
    public ResponseEntity<String> sso() {
        log.info("sso login success");
        return ResponseEntity.ok("sso login success");
    }
}
