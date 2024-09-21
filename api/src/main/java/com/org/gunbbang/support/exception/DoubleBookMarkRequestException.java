package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class DoubleBookMarkRequestException extends HandleException {
  public DoubleBookMarkRequestException(ErrorType errorType) {
    super(errorType);
  }

  public DoubleBookMarkRequestException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
