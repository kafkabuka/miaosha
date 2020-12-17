package com.bnz.miaosha.oauth.service.impl;

import com.bnz.miaosha.oauth.service.AuthService;
import com.bnz.miaosha.oauth.util.AuthToken;
import com.bnz.miaosha.oauth.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RedisUtil redisUtil;

    @Value("${auth.ttl}")
    private long ttl;



    @Override
    public AuthToken login(String username, String password, String clientId, String clientSecret) throws Exception {


        //1.申请令牌
        ServiceInstance serviceInstance = loadBalancerClient.choose("user-oauth");
        URI uri = serviceInstance.getUri();
        String url = uri + "/oauth/token";
        log.info("======== applyLogin -> url={} ========", url);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("username", username);
        body.add("password", password);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        log.info("Authorization: {}:{} -> {}", clientId, clientSecret, this.getHttpBasic(clientId, clientSecret));
        headers.add("Authorization", this.getHttpBasic(clientId, clientSecret));
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() != 400 && response.getRawStatusCode() != 401) {
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Map.class);
        Map map = responseEntity.getBody();
        map.forEach((key, value) -> {
            System.out.println(">>>>>> key:" + key + "\tvalue:" + value);
        });
        if (map.get("access_token") == null || map.get("refresh_token") == null || map.get("jti") == null) {
            //申请令牌失败
            throw new RuntimeException("申请令牌失败");
        }

        //2.封装结果数据
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken( map.get("access_token").toString());
        authToken.setRefreshToken( map.get("refresh_token").toString());
        authToken.setJti( map.get("jti").toString());
        log.info(" authToken.getAccessToken()：{}", authToken.getAccessToken());
        //3.将jti作为redis中的key,将jwt作为redis中的value进行数据的存放
        redisUtil.set(authToken.getJti(), authToken.getAccessToken(), ttl);
        return authToken;
    }

    private String getHttpBasic(String clientId, String clientSecret) throws UnsupportedEncodingException {
        String value = clientId + ":" + clientSecret;
        byte[] encode = Base64Utils.encode(value.getBytes());
        return "Basic " + new String(encode, "utf-8");
    }
}