package com.ctv_it.klb.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class BaseResponseDTO {

  protected String status;
  protected String message;
  protected String url;
  protected LocalDateTime timestamp = LocalDateTime.now();
}
