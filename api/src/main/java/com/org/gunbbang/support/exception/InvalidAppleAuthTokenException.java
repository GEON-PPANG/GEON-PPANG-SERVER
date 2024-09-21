package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class InvalidAppleAuthTokenException extends HandleException {
  public InvalidAppleAuthTokenException(ErrorType errorType) {
    super(errorType);
  }
}
