package com.app.bankingloanservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentRequest {

    @NotNull(message = "Account ID is required")
    private Long accountId;
}