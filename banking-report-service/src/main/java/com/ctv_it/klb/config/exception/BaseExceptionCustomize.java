package com.ctv_it.klb.config.exception;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
public abstract class BaseExceptionCustomize extends RuntimeException {

  protected Object errors;

  public BaseExceptionCustomize() {
    super();
    this.errors = null;
  }

  public BaseExceptionCustomize(String msg) {
    super(msg);
    this.errors = null;
  }

  public BaseExceptionCustomize(String msg, Object errors) {
    super(msg);
    this.errors = errors;
  }
}
