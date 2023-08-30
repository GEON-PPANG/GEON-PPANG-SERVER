package com.org.gunbbang.login.handler;

import com.org.gunbbang.Role;
import com.org.gunbbang.jwt.service.JwtService;
import com.org.gunbbang.login.CustomOAuth2User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    System.out.println("OAuth2 Login 성공!");
    log.info("OAuth2 Login 성공!");
    try {
      CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
      System.out.println("oAuth2User = " + oAuth2User);

      // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 닉네임 설정으로 갈 수 있도록 값을 알려줌
      // TODO: 닉네임 업데이트 시 Role.GUEST인 경우에는 변경 완료되면 Role을 업데이트해야함
      if (oAuth2User.getRole() == Role.GUEST) {
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail(), 6L);
        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
      } else {
        loginSuccess(response, oAuth2User);
      }
    } catch (Exception e) {
      throw e;
    }
  }

  private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
      throws IOException {
    String accessToken =
        jwtService.createAccessToken(oAuth2User.getEmail(), oAuth2User.getMemberId());
    String refreshToken = jwtService.createRefreshToken();
    response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
    response.addHeader(jwtService.getRefreshHeader(), "Bearer " + accessToken);

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);
    jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);
  }
}
