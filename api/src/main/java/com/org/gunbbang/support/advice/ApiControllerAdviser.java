package com.org.gunbbang.support.advice;

import com.org.gunbbang.HandleException;
import com.org.gunbbang.common.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiControllerAdviser {
    @ExceptionHandler(HandleException.class)
    protected ResponseEntity<ApiResponse> handleCustomException(HandleException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(ApiResponse.error(e.getErrorType()));
    }
}
