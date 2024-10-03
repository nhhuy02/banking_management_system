package com.ojt.klb.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ojt.klb.dto.LoginDto;
import com.ojt.klb.dto.RegisterDto;
import com.ojt.klb.dto.RegisterResponseDto;
import com.ojt.klb.model.User;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface UserService {
    Optional<LoginDto> login(String username, String password);

    RegisterResponseDto createUser(RegisterDto registerDto);
    void forgetPasswordGetCode (String phoneNumber) throws JsonProcessingException;
    void changePassword(String phoneNumber, String password);
}
