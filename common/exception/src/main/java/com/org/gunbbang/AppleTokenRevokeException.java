package com.org.gunbbang;

import com.org.gunbbang.errorType.ErrorType;

public class AppleTokenRevokeException extends HandleException {

  public AppleTokenRevokeException(ErrorType errorType) {
    super(errorType);
  }

  public AppleTokenRevokeException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
