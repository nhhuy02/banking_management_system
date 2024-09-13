package com.ctv_it.klb.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString
public abstract class BaseResponseDTO {

  protected boolean success;
  protected int status;
  protected String message;
  protected String url;
  @Builder.Default
  protected LocalDateTime timestamp = LocalDateTime.now();
}
