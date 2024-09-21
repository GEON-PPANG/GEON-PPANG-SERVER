package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class NotFoundException extends HandleException {
  public NotFoundException(ErrorType errorType) {
    super(errorType);
  }

  public NotFoundException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
