package com.ctv_it.klb.dto.base;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
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
