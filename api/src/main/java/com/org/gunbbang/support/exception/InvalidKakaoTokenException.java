package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class InvalidKakaoTokenException extends HandleException {
  public InvalidKakaoTokenException(ErrorType errorType) {
    super(errorType);
  }

  public InvalidKakaoTokenException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
