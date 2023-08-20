package com.org.gunbbang.common.dto;

import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.errorType.SuccessType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponse<T> {

  private final int code;
  private final String message;
  private T data;

  public static ApiResponse success(SuccessType success) {
    return new ApiResponse<>(success.getHttpStatusCode(), success.getMessage());
  }

  public static <T> ApiResponse<T> success(SuccessType success, T data) {
    return new ApiResponse<T>(success.getHttpStatusCode(), success.getMessage(), data);
  }

  public static ApiResponse error(ErrorType error) {
    return new ApiResponse<>(error.getHttpStatusCode(), error.getMessage());
  }

  public static ApiResponse error(ErrorType error, String message) {
    return new ApiResponse<>(error.getHttpStatusCode(), message);
  }

  public static ApiResponse error(int errorCode, String message) {
    return new ApiResponse<>(errorCode, message);
  }
}
