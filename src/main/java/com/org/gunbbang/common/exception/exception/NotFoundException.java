package com.org.gunbbang.common.exception.exception;

import com.org.gunbbang.common.exception.error.ErrorType;

public class NotFoundException extends HandleException {
    public NotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
