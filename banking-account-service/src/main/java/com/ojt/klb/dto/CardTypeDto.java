package com.ojt.klb.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CardTypeDto {

    @NotBlank(message = "{cardTypeName.notBlank}")
    @Size(min = 3, max = 50, message = "{cardTypeName.size}")
    private String cardTypeName;

    @NotNull(message = "{nfc.notNull}")
    private Boolean nfc;

    @NotBlank(message = "{description.notBlank}" )
    @Size(max = 255, message = "{description.size}")
    private String description;

    private Boolean IsActive;
}
