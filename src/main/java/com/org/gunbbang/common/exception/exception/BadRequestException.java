package com.org.gunbbang.common.exception.exception;

import com.org.gunbbang.common.exception.error.ErrorType;

public class BadRequestException extends HandleException {
    public BadRequestException(ErrorType errorType) {
        super(errorType);
    }
}
