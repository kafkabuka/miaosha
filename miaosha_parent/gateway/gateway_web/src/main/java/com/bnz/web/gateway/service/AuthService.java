package com.bnz.web.gateway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private RedisTemplate redisTemplate;

    //从cookie中获取jti的值
    public String getJtiFromCookie(ServerHttpRequest request) {
        HttpCookie httpCookie = request.getCookies().getFirst("uid");
        if (httpCookie != null){
            String jti = httpCookie.getValue();
            return jti;
        }
        return null;
    }

    //从redis中获取jwt
    public String getJwtFromRedis(String jti) {
        Object o = redisTemplate.boundValueOps(jti).get();
        String jwt = o == null ? null:o.toString();
        return jwt;
    }
}
