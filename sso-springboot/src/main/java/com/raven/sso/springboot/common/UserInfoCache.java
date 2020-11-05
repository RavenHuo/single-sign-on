package com.raven.sso.springboot.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: huorw
 * @create: 2020-11-05 21:46
 */
public class UserInfoCache {

    private static Map<String, UserInfo> cache;

    private static UserInfoCache userInfoCache;

    private UserInfoCache(){
        cache = new ConcurrentHashMap<>(16);
    }

    public static UserInfoCache getInstance() {
        if (null == userInfoCache) {
            synchronized (UserInfo.class) {
                if (null == userInfoCache) {
                    userInfoCache = new UserInfoCache();
                }
            }
        }
        return userInfoCache;
    }

    public UserInfo get(String key) {
        return cache.get(key);
    }

    public void put(String key, UserInfo userInfo) {
        cache.put(key, userInfo);
    }

    public Boolean ifExist(String key) {
        return cache.containsKey(key);
    }

}
