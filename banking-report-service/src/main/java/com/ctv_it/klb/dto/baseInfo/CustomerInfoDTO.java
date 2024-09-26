package com.ctv_it.klb.dto.baseInfo;


import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString
public class CustomerInfoDTO {

  private Long id;
  private String fullName;
  private String phoneNumber;
  private LocalDate dateOfBirth;
  private String address;
  private String email;
  private KYC kyc;
  private String kycStatus;

  @Setter
  @Getter
  @ToString
  private static class KYC {

    private String documentType;
    private String documentNumber;
  }
}
