package com.ojt.klb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @Size(min = 6, max = 20, message = "{username.size}")
    @NotBlank(message = "{username.notBlank}")
    private String username;

    @Size(min = 3, max = 150, message = "{password.size}")
    @NotBlank(message = "{password.notBlank}")
    private String password;

}
