package com.ojt.klb.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CardRegistrationRequestDto {
    @NotNull
    private String cardTypeName;
}
