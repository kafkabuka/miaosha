package com.bnz.miaosha.oauth.service;


import com.bnz.miaosha.oauth.util.AuthToken;

public interface AuthService {

    AuthToken login(String username, String password, String clientId, String clientSecret) throws Exception;
}
