package com.ctv_it.klb.dto.baseInfo;


import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
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
  private String kycDocumentType;
  private String kycDocumentNumber;
}
