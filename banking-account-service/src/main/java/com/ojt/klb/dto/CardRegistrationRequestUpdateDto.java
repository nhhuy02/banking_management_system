package com.ojt.klb.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardRegistrationRequestUpdateDto {

    @NotNull
    private String status;

    @NotNull
    private String cardTypeName;

    @JsonIgnore
    private LocalDate reviewDate;

    @JsonIgnore
    private String email;

    @NotNull(message = "{notes.notNull}")
    @Size(min = 10, max = 500, message = "{notes.size}")
    private String notes;
}
