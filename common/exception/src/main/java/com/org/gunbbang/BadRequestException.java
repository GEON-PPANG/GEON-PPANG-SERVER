package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class BadRequestException extends HandleException {
  public BadRequestException(ErrorType errorType) {
    super(errorType);
  }

  public BadRequestException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
