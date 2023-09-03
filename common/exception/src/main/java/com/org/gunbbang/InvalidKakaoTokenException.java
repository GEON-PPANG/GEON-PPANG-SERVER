package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class InvalidKakaoTokenException extends HandleException {
  public InvalidKakaoTokenException(ErrorType errorType) {
    super(errorType);
  }

  public InvalidKakaoTokenException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
