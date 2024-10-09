package com.ctv_it.klb.dto.fetch.response.data.customer;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class FetchCustomerDataResponseDTO {

  private Long customerId;
  private String fullName;
  private LocalDate dateOfBirth;
  private String gender;
  private String email;
  private String phoneNumber;
  private String currentAddress;
  private Kyc kyc;
  private String kycStatus;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter
  @Getter
  @ToString
  public static class Kyc {

    private String documentType;
    private String documentNumber;
  }
}

