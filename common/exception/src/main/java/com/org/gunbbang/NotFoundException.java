package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class NotFoundException extends HandleException {
  public NotFoundException(ErrorType errorType) {
    super(errorType);
  }
}
