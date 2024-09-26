package com.ctv_it.klb.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@SuperBuilder
@Setter
@Getter
@ToString
public abstract class BaseResponseDTO {

  @Builder.Default
  protected boolean success = Boolean.TRUE;
  @Builder.Default
  protected int status = HttpStatus.OK.value();
  @Builder.Default
  protected LocalDateTime timestamp = LocalDateTime.now();
  protected String url;
}
