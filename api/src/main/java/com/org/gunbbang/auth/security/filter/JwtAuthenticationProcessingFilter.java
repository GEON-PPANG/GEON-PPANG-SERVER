package com.org.gunbbang.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.support.exception.CustomJwtTokenException;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.auth.jwt.service.JwtService;
import com.org.gunbbang.auth.security.CustomUserDetails;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.support.errorType.SuccessType;
import com.org.gunbbang.repository.MemberRepository;
import java.io.IOException;
import java.util.UUID;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
  private static final String H2_PREFIX = "/h2-console";
  private final JwtService jwtService;
  private final MemberRepository memberRepository;
  private final ObjectMapper objectMapper;
  private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    log.info("JwtAuthenticationProcessingFilter 진입");
    // 헤더에 유효한 refreshToken이 담겨져서 요청된 경우 토큰 재발급 요청 refresh와 access 둘 다 재발급해서 반환
    if (jwtService.isRefreshTokenExist(request) && jwtService.isAccessTokenExist(request)) {
      refreshAccessAndRefreshTokens(request, response);
      return;
    }

    // 일반적인 엑세스 접근 요청
    if (jwtService.isAccessTokenExist(request)) {
      checkAccessTokenAndAuthentication(request);
      //      throw new CustomJwtTokenException(ErrorType.NOT_EXIST_ACCESS_TOKEN_EXCEPTION);
    }

    filterChain.doFilter(request, response);
  }

  private void refreshAccessAndRefreshTokens(
      HttpServletRequest request, HttpServletResponse response) throws IOException {
    log.info("########## 토큰 리프레시 접근 요청 처리 시작 ##########");

    String accessToken = jwtService.extractAccessTokenAsString(request);
    String refreshToken = jwtService.extractRefreshTokenAsString(request);

    // at가 만료되지 않을 경우 에러
    if (!jwtService.isTokenExpired(accessToken)) {
      log.warn("@@@@@@@@@@ 만료되지 않은 엑세스 토큰을 재발급하려는 시도 @@@@@@@@@@");
      Long memberId = jwtService.extractMemberIdClaim(accessToken);
      throw new CustomJwtTokenException(
          ErrorType.NOT_EXPIRED_ACCESS_TOKEN_EXCEPTION,
          ErrorType.NOT_EXPIRED_ACCESS_TOKEN_EXCEPTION.getMessage() + memberId.toString());
    }

    // at 및 rt 둘 다 만료되었을 시 에러 -> 로그인 창으로 이동
    if (jwtService.isTokenExpired(refreshToken)) {
      log.warn("@@@@@@@@@@ 엑세스 및 리프레시 둘 다 만료된 경우 - 로그인 창으로 이동 @@@@@@@@@@");
      throw new CustomJwtTokenException(ErrorType.EXPIRED_REFRESH_AND_ACCESS_EXCEPTION);
    }

    Long memberId = jwtService.extractMemberIdClaimFromExpiredToken(accessToken);
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));

    // 해당 member의 rt가 요청에서 온 rt랑 일치하는지 판단, 일치하지 않는 경우는 이미 해당 rt로 재발급을 한번은 했다는 의미이므로
    // 이 경우에는 해당 rt를 invalid 처리함
    if (foundMember.getRefreshToken() != null
        && !foundMember.getRefreshToken().equals(refreshToken)) {
      log.warn("@@@@@@@@@@ 재발급 용도로 한번 사용된 정황이 의심되는 rt를 다시 사용하려는 시도. 로그인 페이지로 이동 요청 @@@@@@@@@@");
      log.warn("@@@@@@@@@@ 요청으로 온 rt: {} @@@@@@@@@@", refreshToken);
      log.warn("@@@@@@@@@@ DB에서 조회된 rt: {} @@@@@@@@@@", foundMember.getRefreshToken());

      throw new CustomJwtTokenException(
          ErrorType.ABUSED_REFRESH_TOKEN_EXCEPTION,
          ErrorType.ABUSED_REFRESH_TOKEN_EXCEPTION.getMessage() + memberId);
    }

    jwtService.reIssueTokensAndUpdateRefreshToken(response, foundMember);
    ApiResponse.sendSuccessResponseBody(
        response, objectMapper, SuccessType.ISSUE_REFRESH_TOKEN_SUCCESS);
  }

  /**
   * accessToken 유효성 검사 후 해당 토큰에서 추출한 memberId로 회원 객체 조회 성공 시 saveAuthentication() 호출해서
   * Authentication 객체 생성 후 SecurityContext에 저장
   */
  public void checkAccessTokenAndAuthentication(HttpServletRequest request) {
    log.info("########## 일반적인 리소스 접근 요청. 엑세스 토큰 유효성 검사 시작 ##########");

    String accessToken = jwtService.extractAccessTokenAsString(request);
    Long memberId = jwtService.extractMemberIdClaim(accessToken);

    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    saveAuthentication(foundMember);
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
            myMember.getNickname(),
            myMember.getRole().toString());

    log.info("########## 엑세스 요청 성공 시 user id {} ##########", userDetailsUser.getMemberId());

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            userDetailsUser, // principle
            null, // credential (보통 비밀번호. 인증 시에는 null로 들어감)
            authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

    // SecurityContext에 Authentication 객체 저장
    SecurityContextHolder.getContext().setAuthentication(authentication);
    System.out.println("authen: " + SecurityContextHolder.getContext().getAuthentication().toString());
  }
}
