package com.org.gunbbang.jwt.filter;

import com.org.gunbbang.entity.Member;
import com.org.gunbbang.jwt.service.JwtService;
import com.org.gunbbang.login.CustomUserDetails;
import com.org.gunbbang.repository.MemberRepository;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/** 커스텀 JWT 인증 필터 로그인 이외의 api 요청이 왔을 때 해당 요청을 처리함 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
  private static final String WHITE_LIST = "/auth/login"; // 로그인 요청은 필터에서 제외

  private final JwtService jwtService;
  private final MemberRepository memberRepository;

  private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (request.getRequestURI().equals(WHITE_LIST)) {
      filterChain.doFilter(request, response);
      return; // 그대로 return해서 로그인 요청은 다음 필터로 넘김
    }

    String refreshToken =
        jwtService.extractRefreshToken(request).filter(jwtService::isTokenValid).orElse(null);

    /** 헤더에 유효한 refreshToken이 담겨져서 요청된 경우 토큰 재발급 요청 refresh와 access 둘 다 재발급해서 반환 */
    if (refreshToken != null) {
      ReIssueTokensAndUpdateRefreshToken(response, refreshToken);
      return;
    }

    /** refreshToken가 null인 경우 -> 일반적인 인증인 경우 accesssToken이 유효한지 검사 후 유효하면 접근 허용, 유효하지 않으면 에러 응답 */
    if (refreshToken == null) {
      checkAccessTokenAndAuthentication(request, response, filterChain);
    }
  }

  private void ReIssueTokensAndUpdateRefreshToken(
      HttpServletResponse response, String refreshToken) {
    memberRepository
        .findByRefreshToken(refreshToken)
        .ifPresent(
            member -> {
              String reIssuedRefreshToken = reIssueAndUpdateRefreshToken(member);
              String reIssuedAccessToken =
                  jwtService.createAccessToken(member.getEmail(), member.getMemberId());
              jwtService.sendAccessAndRefreshToken(
                  response, reIssuedAccessToken, reIssuedRefreshToken);
            });
  }

  /** refreshToken 재발급하고 디비에 반영 후 flush */
  private String reIssueAndUpdateRefreshToken(Member member) {
    String reIssuedRefreshToken = jwtService.createRefreshToken();
    member.updateRefreshToken(reIssuedRefreshToken);
    memberRepository.saveAndFlush(member);
    return reIssuedRefreshToken;
  }

  /**
   * accessToken 유효성 검사 후 해당 토큰에서 추출한 memberId로 회원 객체 조회 성공 시 saveAuthentication() 호출해서
   * Authentication 객체 생성 후 SecurityContext에 저장
   */
  public void checkAccessTokenAndAuthentication(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("엑세스 토큰 유효성 검사 시작");
    jwtService
        .extractAccessToken(request)
        .filter(jwtService::isTokenValid)
        .ifPresent(
            accessToken ->
                jwtService
                    .extractMemberIdClaim(accessToken)
                    .ifPresent(
                        memberId ->
                            memberRepository
                                .findById(memberId)
                                .ifPresent(this::saveAuthentication)));

    filterChain.doFilter(request, response);
  }

  /** SecurityContext에 Authentication 객체를 저장 */
  public void saveAuthentication(Member myMember) {
    String password = myMember.getPassword();
    if (password == null) {
      password = UUID.randomUUID().toString();
    }

    CustomUserDetails userDetailsUser =
        new CustomUserDetails(
            myMember.getEmail(),
            myMember.getPassword(),
            myMember.getMemberId(),
            myMember.getMainPurpose(),
            myMember.getBreadType().getBreadTypeId(),
            myMember.getNutrientType().getNutrientTypeId(),
            myMember.getNickname());

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            userDetailsUser, // principle
            null, // credential (보통 비밀번호. 인증 시에는 null로 들어감)
            authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

    // SecurityContext에 Authentication 객체 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
