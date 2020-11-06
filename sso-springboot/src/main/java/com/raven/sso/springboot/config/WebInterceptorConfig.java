package com.raven.sso.springboot.config;

import com.raven.sso.springboot.interceptor.SsoInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-06 21:01
 */
@Component
public class WebInterceptorConfig extends WebMvcConfigurationSupport {

    @Autowired
    private SsoInterceptor ssoInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ssoInterceptor);
        super.addInterceptors(registry);

    }
}
