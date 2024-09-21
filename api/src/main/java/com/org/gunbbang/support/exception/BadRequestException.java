package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class BadRequestException extends HandleException {
  public BadRequestException(ErrorType errorType) {
    super(errorType);
  }

  public BadRequestException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
