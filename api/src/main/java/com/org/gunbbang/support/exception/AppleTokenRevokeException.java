package com.org.gunbbang.support.exception;

import com.org.gunbbang.support.errorType.ErrorType;

public class AppleTokenRevokeException extends HandleException {

  public AppleTokenRevokeException(ErrorType errorType) {
    super(errorType);
  }

  public AppleTokenRevokeException(ErrorType errorType, String message) {
    super(errorType, message);
  }
}
