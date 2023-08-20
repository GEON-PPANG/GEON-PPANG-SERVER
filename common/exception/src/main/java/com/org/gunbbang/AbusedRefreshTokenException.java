package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class AbusedRefreshTokenException extends HandleException {
  public AbusedRefreshTokenException(ErrorType errorType) {
    super(errorType);
  }
}
