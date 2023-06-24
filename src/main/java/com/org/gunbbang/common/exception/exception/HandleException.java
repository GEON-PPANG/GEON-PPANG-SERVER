package com.org.gunbbang.common.exception.exception;

import com.org.gunbbang.common.exception.error.ErrorType;

public class HandleException extends RuntimeException {
    private final ErrorType errorType;

    public HandleException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public int getHttpStatus() {
        return errorType.getHttpStatusCode();
    }

}
