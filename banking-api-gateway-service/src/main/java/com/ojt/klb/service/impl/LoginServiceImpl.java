package com.ojt.klb.service.impl;

import com.ojt.klb.client.Client;
import com.ojt.klb.dto.DataForJwt;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.AuthLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements AuthLoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
    private final Client client;
    private final AuthServiceHelper authServiceHelper;

    public LoginServiceImpl(Client client, AuthServiceHelper authServiceHelper) {
        this.client = client;
        this.authServiceHelper = authServiceHelper;
    }

    @Override
    public String VerifyLoginAndGenJWT(String username, String password) {
        logger.info("Attempting to login for username: {}, password: {}", username, password);

        ResponseEntity<ApiResponse<DataForJwt>> response = client.getDataLogin(username, password);
        logger.info("Response body: {}", response.getBody());

        if (response.getBody() != null && response.getBody().isSuccess()) {
            DataForJwt loginRequest = response.getBody().getData();
            return authServiceHelper.generateTokenAndFetchIds(loginRequest);
        } else {
            logger.warn("Login failed for username: {}. Response: {}", username, response);
            return null;
        }
    }
}
