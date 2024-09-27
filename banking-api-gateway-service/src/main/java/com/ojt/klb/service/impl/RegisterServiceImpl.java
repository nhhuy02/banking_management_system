package com.ojt.klb.service.impl;

import com.ojt.klb.client.Client;
import com.ojt.klb.dto.DataForJwt;
import com.ojt.klb.dto.RegisterRequest;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.AuthRegisterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class RegisterServiceImpl implements AuthRegisterService {

    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    private final Client client;
    private final AuthServiceHelper authServiceHelper;

    public RegisterServiceImpl(Client client, AuthServiceHelper authServiceHelper) {
        this.client = client;
        this.authServiceHelper = authServiceHelper;
    }

    @Override
    public String VerifyRegisterAndGenJWT(RegisterRequest registerRequest) {
        logger.info("Received register data: {}", registerRequest);

        ResponseEntity<ApiResponse<DataForJwt>> response = client.registerData(registerRequest);
        logger.info("Response body: {}", response.getBody());

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            logger.error("Sleep interrupted", e);
            return null;
        }

        if (response.getBody() != null && response.getBody().isSuccess()) {
            DataForJwt registerData = response.getBody().getData();
            return authServiceHelper.generateTokenAndFetchIds(registerData);
        } else {
            logger.warn("Register fail: {}", response);
            return null;
        }
    }

}
