package com.raven.sso.springboot.common;

import lombok.Builder;
import lombok.Data;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-05 21:46
 */
@Data
@Builder
public class UserInfo {

    private String name;

    private String phone;
}
