package com.org.gunbbang.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.support.errorType.ErrorType;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/** 로그인 실패 시 호출되는 핸들러 */
@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException {
    log.warn("@@@@@@@@@@ 로그인 실패. error message: {} @@@@@@@@@@", exception.getMessage());
    ApiResponse.sendErrorResponseBody(response, objectMapper, ErrorType.LOGIN_FAIL_EXCEPTION);
  }
}
