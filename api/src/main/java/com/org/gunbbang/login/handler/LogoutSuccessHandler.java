package com.org.gunbbang.login.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.errorType.SuccessType;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

@Slf4j
@RequiredArgsConstructor
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
  private final ObjectMapper objectMapper;

  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    log.info("LogoutSuccessHandler 진입");

    response.setStatus(HttpServletResponse.SC_OK);
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");

    // ApiResponse를 사용하여 JSON 응답 생성
    ApiResponse responseBody = ApiResponse.success(SuccessType.LOGOUT_SUCCESS);
    String jsonResponse = objectMapper.writeValueAsString(responseBody);

    response.getWriter().write(jsonResponse);
  }
}
