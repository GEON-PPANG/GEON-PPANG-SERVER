package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class DoubleBookMarkRequestException extends HandleException {
  public DoubleBookMarkRequestException(ErrorType errorType) {
    super(errorType);
  }

  public DoubleBookMarkRequestException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
