package com.org.gunbbang.common.DTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.support.errorType.SuccessType;
import java.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
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

  public static void sendSuccessResponseBody(
      HttpServletResponse response, ObjectMapper objectMapper, SuccessType successType)
      throws IOException {
    response.setStatus(successType.getHttpStatusCode());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    ApiResponse responseBody = ApiResponse.success(successType);
    String jsonResponse = objectMapper.writeValueAsString(responseBody);

    response.getWriter().write(jsonResponse);
  }

  public static void sendErrorResponseBody(
      HttpServletResponse response, ObjectMapper objectMapper, ErrorType errorType)
      throws IOException {
    response.setStatus(errorType.getHttpStatusCode());
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    ApiResponse responseBody = ApiResponse.error(errorType);
    String jsonResponse = objectMapper.writeValueAsString(responseBody);

    response.getWriter().write(jsonResponse);
  }
}
