package com.ojt.klb.controller;

import com.ojt.klb.dto.LoginRequest;
import com.ojt.klb.dto.RegisterRequest;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.AuthLoginService;
import com.ojt.klb.service.AuthRegisterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final AuthLoginService authLoginService;
    private final AuthRegisterService authRegisterService;

    public AuthController(AuthLoginService authLoginService, AuthRegisterService authRegisterService) {
        this.authLoginService = authLoginService;
        this.authRegisterService = authRegisterService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody LoginRequest loginRequest) {
        String token = authLoginService.VerifyLoginAndGenJWT(loginRequest.getUsername(), loginRequest.getPassword());
        return getApiResponseResponseEntity(token);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<String>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        String token = authRegisterService.VerifyRegisterAndGenJWT(registerRequest);
        return getApiResponseResponseEntity(token);
    }

    private ResponseEntity<ApiResponse<String>> getApiResponseResponseEntity(String token) {
        ApiResponse<String> response;

        if (token != null) {
            response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Successful",
                    true,
                    token
            );
            return ResponseEntity.ok(response);
        } else {
            response = new ApiResponse<>(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Invalid credentials",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}

