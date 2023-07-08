package com.org.gunbbang;


import com.org.gunbbang.errorType.ErrorType;

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
