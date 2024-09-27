package com.ojt.klb.controller;

import com.ojt.klb.dto.ForgetPasswordDto;
import com.ojt.klb.dto.LoginDto;
import com.ojt.klb.dto.RegisterDto;
import com.ojt.klb.dto.RegisterResponseDto;
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
@RequestMapping("/api/v1/users")
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


    @PostMapping("/forgetPassword/{userId}")
    public ResponseEntity<ApiResponse<String>> forgetPassword(
            @PathVariable Long userId,
            @Valid @RequestBody ForgetPasswordDto forgetPasswordDto) {

        logger.info("Forget password request received for user ID: {}", userId);

        try {
            userService.forgetPassword(userId, forgetPasswordDto.getNewPassword());

            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Password updated successfully",
                    true,
                    null
            );
            return ResponseEntity.ok(response);

        } catch (UserNotFoundException e) {
            logger.warn("User not found with ID: {}", userId);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "User not found",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            logger.error("Error updating password for user ID: {}", userId, e);
            ApiResponse<String> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An error occurred while updating the password",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}

