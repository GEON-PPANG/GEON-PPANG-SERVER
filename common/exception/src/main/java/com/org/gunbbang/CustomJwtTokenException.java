package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class CustomJwtTokenException extends HandleException {

  public CustomJwtTokenException(ErrorType errorType) {
    super(errorType);
  }

  public CustomJwtTokenException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
