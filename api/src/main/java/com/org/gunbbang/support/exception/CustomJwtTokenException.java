package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class CustomJwtTokenException extends HandleException {

  public CustomJwtTokenException(ErrorType errorType) {
    super(errorType);
  }

  public CustomJwtTokenException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
