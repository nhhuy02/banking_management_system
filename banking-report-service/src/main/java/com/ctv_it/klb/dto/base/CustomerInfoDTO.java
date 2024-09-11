package com.ctv_it.klb.dto.base;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString
public class CustomerInfoDTO {

  private UUID id;
  private String name;
  private String phoneNumber;
  private String address;
  private String nationalId;
}
