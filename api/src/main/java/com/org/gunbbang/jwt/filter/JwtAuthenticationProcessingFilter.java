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

/**
 * JWT 인증 필터 "/login" 이외의 uri 요청이 왔을 때 해당 요청을 처리함 (로그인 요청은 엑세서 or 리프레시 토큰 받기 전이니까) refreshToken은 토큰
 * 재발급 요청 시에만 오고 일반적으로는 엑세스 토큰만 와서 인증 요청함
 *
 * <p>[일반적인 인증 요청] 1. refreshToken이 없고 accessToken이 유효한 경우 -> 인증 성공 [잘못된 인증 요청] 2. refreshToken이 없고
 * accessToken이 없거나 유효하지 않은 경우 -> 잘못된 인증 요청, 인증 실패, 403 반환 [토큰 재발급 요청] 3. refreshToken이 있는 경우 -> db에
 * refreshToken 조회해서 일치하면 둘 다 재발급, 조회 못하면 에러처리 TODO: 3번 관련해서 해당 요청에 accessToken이 아직 만료 안된거라면 400에러
 * 떨궈야되는거 아닌가?
 */
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

    // 헤더에서 refreshToken 추출
    String refreshToken =
        jwtService.extractRefreshToken(request).filter(jwtService::isTokenValid).orElse(null);

    /** 헤더에 유효한 refreshToken이 담겨져서 요청된 경우 -> 토큰 재발급 요청 refresh와 access 둘 다 재발급해서 반환 */
    if (refreshToken != null) {
      checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
      return;
    }

    /**
     * refreshToken가 null인 경우 -> 일반적인 인증 요청 이지만 accesssToken이 유효하지 않을 경우 403을 내려줘야함 accessToken 유효성
     * 검사
     */
    if (refreshToken == null) {
      checkAccessTokenAndAuthentication(request, response, filterChain);
    }
  }

  // 해당 refreshToken으로 db에 조회해서 회원을 찾음
  // 찾은 회원의 refreshToken 재발급
  private void checkRefreshTokenAndReIssueAccessToken(
      HttpServletResponse response, String refreshToken) {
    memberRepository
        .findByRefreshToken(refreshToken)
        .ifPresent(
            member -> {
              String reIssuedRefreshToken = reIssueRefreshToken(member);
              String reIssuedAccessToken =
                  jwtService.createAccessToken(member.getEmail(), member.getMemberId());
              jwtService.sendAccessAndRefreshToken(
                  response, reIssuedAccessToken, reIssuedRefreshToken);
            });
  }

  // refreshToken 재발급하고 디비에 반영 후 flush
  private String reIssueRefreshToken(Member member) {
    String reIssuedRefreshToken = jwtService.createRefreshToken();
    member.updateRefreshToken(reIssuedRefreshToken);
    memberRepository.saveAndFlush(member);
    return reIssuedRefreshToken;
  }

  /**
   * accessToken 추출하고 유효한지 검증 유효하면 claim에서 memberId 추출하고 해당 memberId 조회된 유저 객체 반환 해당 유저 객체를
   * saveAuthentication()으로 인증처리하여 인증 허가 처리된 객체를 SecurityContextHolder에 담기 그 이후 다음 인증 필터로 넘김
   */
  public void checkAccessTokenAndAuthentication(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("checkAccessTokenAndAuthentication() 호출");
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

  /**
   * UserDetails의 User를 Builder로 생성 후 해당 객체를 인증 처리하여 해당 유저 객체를 SecurityContextHolder에 담아 인증 처리를 진행
   */
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
            myMember.getNutrientType().getNutrientTypeId());

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(
            userDetailsUser, // 유저 정보 지정
            null, // credential (보통 비밀번호. 인증 시에는 null로 들어감)
            authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

    // SecurityContext를 SecurityContextHolder에서 꺼낸 뒤 위에서 만든 Authentication 객체에 대한 인증 허가 처리
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
