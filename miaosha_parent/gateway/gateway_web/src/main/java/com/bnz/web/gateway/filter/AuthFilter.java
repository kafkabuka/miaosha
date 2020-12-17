package com.bnz.web.gateway.filter;

import com.bnz.web.gateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

@Component
@Slf4j
public class AuthFilter implements GlobalFilter, Ordered {

    private static final String LOGIN_URL="http://127.0.0.1:8080";

    @Autowired
    private AuthService authService;

    /**
     * 全局过滤器 核心方法
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();

        //1.判断当前请求路径是否为登录请求,如果是,则直接放行
        String path = request.getURI().getPath();
        if ("/oauth/login".equals(path) || !UrlFilter.hasAuthorize(path)){
            //直接放行
            return chain.filter(exchange);
        }

        //2.从cookie中获取jti的值,如果该值不存在,拒绝本次访问
        String jti = authService.getJtiFromCookie(request);
        log.info("jti：{}",jti);
        if (StringUtils.isEmpty(jti)){
            //拒绝访问
            /*response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();*/
            //跳转登录页面
//            return this.toLoginPage(LOGIN_URL+"?FROM="+request.getURI().getPath(),exchange);
            return this.toLoginPage(LOGIN_URL,exchange);
        }

        //3.从redis中获取jwt的值,如果该值不存在,拒绝本次访问
        String jwt = authService.getJwtFromRedis(jti);
        log.info("jwt：{}",jwt);
        if (StringUtils.isEmpty(jwt)){
            //拒绝访问
            /*response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();*/
            return this.toLoginPage(LOGIN_URL,exchange);
        }
        String authorization = "Bearer "+jwt;
        Consumer<HttpHeaders> httpHeaders = httpHeader -> {
            httpHeader.set("Authorization", authorization);
        };
        // 4.对当前的请求对象进行增强,让它会携带令牌的信息
        ServerHttpRequest host = request.mutate().headers(httpHeaders).build();
        ServerWebExchange build = exchange.mutate().request(host).build();
        return chain.filter(build);
    }

    //跳转登录页面
    private Mono<Void> toLoginPage(String loginUrl, ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        byte[] bytes = "{\"status\":\"-1\",\"msg\":\"error\"}".getBytes(StandardCharsets.UTF_8);
        DataBuffer buff = response.bufferFactory().wrap(bytes);
        response.writeWith(Flux.just(buff));
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
