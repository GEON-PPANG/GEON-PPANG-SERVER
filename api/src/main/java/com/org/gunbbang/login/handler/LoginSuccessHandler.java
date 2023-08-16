package com.org.gunbbang.login.handler;

import com.org.gunbbang.jwt.service.JwtService;
import com.org.gunbbang.login.CustomUserDetails;
import com.org.gunbbang.repository.MemberRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/** 로그인 성공 시 호출되는 handler */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JwtService jwtService;
  private final MemberRepository memberRepository;

  @Value("${jwt.access.expiration}")
  private String accessTokenExpiration;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String email = extractUsername(authentication);
    Long memberId = extractMemberId(authentication);
    String accessToken = jwtService.createAccessToken(email, memberId);
    String refreshToken = jwtService.createRefreshToken();

    // accessToken 및 refreshToken 헤더에 전송
    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

    memberRepository
        .findByEmail(email)
        .ifPresent(
            member -> {
              member.updateRefreshToken(refreshToken);
              memberRepository.saveAndFlush(member);
            });
    log.info("로그인 요청 성공. 이메일 : {} memberId : {} ", email, memberId);
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
