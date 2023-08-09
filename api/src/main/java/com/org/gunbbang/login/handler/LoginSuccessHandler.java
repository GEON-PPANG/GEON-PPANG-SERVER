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

/** 로그인 성공 시 처리되는 handler */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JwtService jwtService;
  private final MemberRepository memberRepository;

  @Value("${jwt.access.expiration")
  private String accessTokenExpiration;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    String email = extractUsername(authentication); // 인증 정보에서 Username(햔재 코드 상 email) 추출
    Long memberId = extractMemberId(authentication);
    String accessToken = jwtService.createAccessToken(email, memberId); // 여기 memberId 들어가야함
    String refreshToken = jwtService.createRefreshToken(); // refreshToken 발급

    jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

    memberRepository
        .findByEmail(email)
        .ifPresent(
            member -> {
              member.updateRefreshToken(refreshToken);
              memberRepository.saveAndFlush(member);
            });
    log.info("로그인에 성공하였습니다. 이메일 : {}", email);
    log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
    log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
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
