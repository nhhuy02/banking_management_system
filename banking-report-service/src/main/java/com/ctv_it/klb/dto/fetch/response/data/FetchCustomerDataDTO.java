package com.ctv_it.klb.dto.fetch.response.data;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class FetchCustomerDataDTO {

  private Long id;
  private Long accountId;
  private String fullName;
  private LocalDate dateOfBirth;
  private String gender;
  private String email;
  private String phoneNumber;
  private String currentAddress;
  private Kyc kyc;
  private String kycStatus;


  @Builder
  @Setter
  @Getter
  @ToString
  public static class Kyc {

    private String documentType;
    private String documentNumber;
  }
}

