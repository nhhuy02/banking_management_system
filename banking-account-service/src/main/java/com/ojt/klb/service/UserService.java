package com.ojt.klb.service;

import com.ojt.klb.dto.LoginDto;
import com.ojt.klb.dto.RegisterDto;
import com.ojt.klb.dto.RegisterResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserService {
    Optional<LoginDto> login(String username, String password);

//    void createUser (RegisterDto registerDto);
    RegisterResponseDto createUser(RegisterDto registerDto);
    void forgetPassword (Long userId, String newPassword);
}
