package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class NotFoundException extends HandleException {
  public NotFoundException(ErrorType errorType) {
    super(errorType);
  }

  public NotFoundException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
