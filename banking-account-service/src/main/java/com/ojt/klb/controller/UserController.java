package com.ojt.klb.controller;

import com.ojt.klb.dto.*;
import com.ojt.klb.exception.AccountClosedException;
import com.ojt.klb.exception.AccountSuspendedException;
import com.ojt.klb.exception.PhoneNumberAlreadyExistsException;
import com.ojt.klb.exception.UserNotFoundException;
import com.ojt.klb.service.UserService;
import com.ojt.klb.response.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account/users")
@Validated
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginDto>> login(@RequestParam String username, @RequestParam String password) {
        logger.info("Login request received for username: {}", username);

        try {
            Optional<LoginDto> loginDto = userService.login(username, password);

            if (loginDto.isPresent()) {
                ApiResponse<LoginDto> response = new ApiResponse<>(
                        HttpStatus.OK.value(),
                        "Login successful",
                        true,
                        loginDto.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<LoginDto> response = new ApiResponse<>(
                        HttpStatus.UNAUTHORIZED.value(),
                        "Invalid username or password",
                        false,
                        null
                );
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (AccountSuspendedException | AccountClosedException e) {
            ApiResponse<LoginDto> response = new ApiResponse<>(
                    HttpStatus.FORBIDDEN.value(),
                    e.getMessage(),
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);

        }
    }


    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponseDto>> createUser(@Valid @RequestBody RegisterDto registerDto) {
        logger.info("Create user request received for username: {}", registerDto.getPhoneNumber());
        try {
            RegisterResponseDto userResponse = userService.createUser(registerDto);

            ApiResponse<RegisterResponseDto> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "User created successfully",
                    true,
                    userResponse
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (PhoneNumberAlreadyExistsException e) {
            ApiResponse<RegisterResponseDto> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Phone number already exists",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (IllegalArgumentException e) {
            ApiResponse<RegisterResponseDto> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage());
            ApiResponse<RegisterResponseDto> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error creating user",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/forgetPassword/code/{phoneNumber}")
    public ResponseEntity<ApiResponse<String>> forgetPasswordGetCode(@PathVariable String phoneNumber) {
        logger.info("Forget password code request received for phone number: {}", phoneNumber);
        try {
            userService.forgetPasswordGetCode(phoneNumber);

            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Password reset code sent successfully",
                    true,
                    null
            );
            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            logger.warn("User not found with phone number: {}", phoneNumber);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "User not found",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            logger.error("Error sending password reset code for phone number: {}", phoneNumber, e);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while sending the password reset code",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto) {
        try {
            userService.changePassword(changePasswordDto.getPhoneNumber(), changePasswordDto.getPassword());

            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Change password success",
                    true,
                    null
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (UserNotFoundException e) {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "PhoneNumber not found",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (IllegalArgumentException e) {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while updating the password.",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}

