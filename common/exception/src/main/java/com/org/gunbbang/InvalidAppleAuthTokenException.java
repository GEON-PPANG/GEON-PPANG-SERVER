package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class InvalidAppleAuthTokenException extends HandleException {
  public InvalidAppleAuthTokenException(ErrorType errorType) {
    super(errorType);
  }
}
