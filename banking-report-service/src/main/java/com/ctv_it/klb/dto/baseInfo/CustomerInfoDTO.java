package com.ctv_it.klb.dto.baseInfo;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString
public class CustomerInfoDTO {

  private Long id;
  private String name;
  private String phoneNumber;
  private String address;
  private String nationalId;
  private String email;
}
