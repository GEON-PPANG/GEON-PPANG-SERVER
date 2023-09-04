package com.org.gunbbang.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.errorType.ErrorType;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    response.setStatus(HttpServletResponse.SC_OK);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    // ApiResponse를 사용하여 JSON 응답 생성
    ApiResponse responseBody = ApiResponse.error(ErrorType.LOGOUT_FAIL_EXCEPTION);
    String jsonResponse = objectMapper.writeValueAsString(responseBody);

    response.getWriter().write(jsonResponse);
  }
}
