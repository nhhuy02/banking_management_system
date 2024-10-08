package com.ojt.klb.service.impl;

import com.ojt.klb.client.Client;
import com.ojt.klb.dto.DataForJwt;
import com.ojt.klb.dto.IdDto;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceHelper {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceHelper.class);
    private final JwtService jwtService;
    private final Client client;

    public AuthServiceHelper(JwtService jwtService, Client client) {
        this.jwtService = jwtService;
        this.client = client;
    }

    public String generateTokenAndFetchIds(DataForJwt dataForJwt) {
        return getString(dataForJwt, client, jwtService);
    }

    static String getString(DataForJwt dataForJwt, Client client, JwtService jwtService) {
        ResponseEntity<ApiResponse<IdDto>> dataResponse = client.getAllId(dataForJwt.getId());
        AuthServiceHelper.logger.info("Received response for user ID: {} from account service: {}", dataForJwt.getId(),
                dataResponse);

        if (dataResponse.getBody() != null && dataResponse.getBody().isSuccess()) {

            IdDto idDto = dataResponse.getBody().getData();
            String token = jwtService.createToken(
                    dataForJwt.getUsername(),
                    idDto.getUserId(),
                    idDto.getAccountId(),
                    dataForJwt.getRole(),
                    idDto.getCustomerId(),
                    idDto.getSavingAccountId(),
                    idDto.getAccountNumber());
            AuthServiceHelper.logger.info("JWT token created successfully for username: {}", dataForJwt.getUsername());
            return token;
        } else {
            AuthServiceHelper.logger.warn("Failed to get all IDs for user ID: {}", dataForJwt.getId());
            return null;
        }
    }
}
