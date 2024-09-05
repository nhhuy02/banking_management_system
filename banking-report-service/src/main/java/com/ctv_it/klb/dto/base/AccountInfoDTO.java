package com.ctv_it.klb.dto.base;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountInfoDTO {

  private UUID uuid;
  private String type;
  private String number;
  private double balance;
  private String currency;
  private String status;
  private String availableBalance;
  private String branch;
}
