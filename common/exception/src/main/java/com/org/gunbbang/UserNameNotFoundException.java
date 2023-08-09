package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class UserNameNotFoundException extends HandleException {
  public UserNameNotFoundException(ErrorType errorType) {
    super(errorType);
  }
}
