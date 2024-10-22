package com.ojt.klb.service;

import org.springframework.stereotype.Service;

@Service
public interface AuthLoginService {
    String VerifyLoginAndGenJWT(String username, String password);
    void logout(String token);
    boolean isTokenBlacklisted(String token);
}
