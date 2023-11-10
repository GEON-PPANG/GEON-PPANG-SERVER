package com.org.gunbbang.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.errorType.SuccessType;
import java.io.IOException;
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
      throws IOException {
    log.info("########## LogoutSuccessHandler 진입 ##########");
    ApiResponse.sendSuccessResponseBody(response, objectMapper, SuccessType.LOGOUT_SUCCESS);
  }
}
