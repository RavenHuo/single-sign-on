package com.raven.sso.zuul;

import com.raven.sso.zuul.filter.SsoZuulFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@EnableZuulProxy
@EnableDiscoveryClient
@EnableEurekaClient
public class SsoSpringcloudZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsoSpringcloudZuulApplication.class, args);
    }

    @Bean
    public SsoZuulFilter ssoZuulFilter(){
        return new SsoZuulFilter();
    }
}
