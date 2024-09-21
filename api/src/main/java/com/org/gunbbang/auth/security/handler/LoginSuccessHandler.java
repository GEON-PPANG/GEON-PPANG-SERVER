package com.org.gunbbang.auth.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.auth.jwt.service.JwtService;
import com.org.gunbbang.auth.security.CustomUserDetails;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.support.errorType.SuccessType;
import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/** 로그인 성공 시 호출되는 handler */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JwtService jwtService;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String email = extractUsername(authentication);
    Long memberId = extractMemberId(authentication);
    String accessToken = jwtService.createAccessToken(email, memberId);
    String refreshToken = jwtService.createRefreshToken();

    // accessToken 및 refreshToken 헤더에 전송
    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.updateRefreshTokenByMemberId(memberId, refreshToken);

    ApiResponse.sendSuccessResponseBody(response, objectMapper, SuccessType.LOGIN_SUCCESS);
    log.info("########## 로그인 요청 성공. 이메일 : {} memberId : {} ########## ", email, memberId);
  }

  private String extractUsername(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    return userDetails.getUsername();
  }

  private Long extractMemberId(Authentication authentication) {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    Long memberId = ((CustomUserDetails) userDetails).getMemberId();
    return memberId;
  }
}
