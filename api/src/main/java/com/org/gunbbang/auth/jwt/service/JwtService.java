package com.org.gunbbang.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import java.util.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {
  @Value("${jwt.secretKey}")
  private String SECRET_KEY;

  @Value("${jwt.access.expiration}")
  private Long ACCESS_TOKEN_EXPIRE_PERIOD;

  @Value("${jwt.refresh.expiration}")
  private Long REFRESH_TOKEN_EXPIRE_PERIOD;

  @Value("${jwt.access.header}")
  private String ACCESS_HEADER;

  @Value("${jwt.refresh.header}")
  private String REFRESH_HEADER;

  @Value("${apple.refresh.header}")
  private String APLLE_REFRESH_HEADER;

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
  private static final String APPLE_REFRESH_TOKEN_SUBJECT = "Apple-refresh";
  private static final String EMAIL_CLAIM = "email";
  private static final String MEMBER_ID_CLAIM = "memberId";
  private static final String BEARER_PREFIX = "Bearer ";

  private final MemberRepository memberRepository;
  private final ObjectMapper objectMapper;

  public String createAccessToken(String email, Long memberId) {
    Date now = new Date();
    return JWT.create()
        .withSubject(ACCESS_TOKEN_SUBJECT) // jwt의 subject 지정 (AccessToken으로 지정)
        .withExpiresAt(new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_PERIOD)) // 토큰 만료시간 지정
        .withClaim(EMAIL_CLAIM, email)
        .withClaim(MEMBER_ID_CLAIM, memberId)
        .sign(Algorithm.HMAC512(SECRET_KEY));
  }

  // refreshToken 생성
  public String createRefreshToken() {
    Date now = new Date();
    return JWT.create()
        .withSubject(REFRESH_TOKEN_SUBJECT)
        .withExpiresAt(new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_PERIOD))
        .sign(Algorithm.HMAC512(SECRET_KEY));
  }

  public void setSignedUpMemberToken(SignedUpMemberVO vo, HttpServletResponse response) {
    String accessToken = createAccessToken(vo.getEmail(), vo.getMemberId());
    response.setHeader(ACCESS_HEADER, accessToken);

    if (vo.getRole().equals(Role.ROLE_MEMBER)) {
      String refreshToken = createRefreshToken();
      updateRefreshTokenByMemberId(vo.getMemberId(), refreshToken);
      response.setHeader(REFRESH_HEADER, refreshToken);
    }

    if (vo.getPlatformType().equals(PlatformType.APPLE)) {
      response.setHeader(APLLE_REFRESH_HEADER, vo.getAppleRefreshToken());
    }
  }

  public void reIssueTokensAndUpdateRefreshToken(HttpServletResponse response, Member foundMember) {
    String reIssuedRefreshToken = reIssueAndUpdateRefreshToken(foundMember);
    String reIssuedAccessToken =
        createAccessToken(foundMember.getEmail(), foundMember.getMemberId());
    sendAccessAndRefreshToken(response, reIssuedAccessToken, reIssuedRefreshToken);
    log.info("########## 엑세스 토큰 및 리프레시 토큰 재발급 완료 ##########");
  }

  /** GUEST였던 회원이 닉네임 변경하고 USER로 되었을 때 사용 */
  @Transactional
  public void reIssueTokensAndUpdateRefreshToken(HttpServletResponse response, Long memberId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    reIssueTokensAndUpdateRefreshToken(response, foundMember);
  }

  private String reIssueAndUpdateRefreshToken(Member member) {
    String reIssuedRefreshToken = createRefreshToken();
    member.updateRefreshToken(reIssuedRefreshToken);
    memberRepository.saveAndFlush(member);
    return reIssuedRefreshToken;
  }

  /** accessToken, refreshToken 재발급 후 header에 넣어서 리턴 */
  public void sendAccessAndRefreshToken(
      HttpServletResponse response, String accessToken, String refreshToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    setAccessTokenHeader(response, accessToken);
    setRefreshTokenHeader(response, refreshToken);
    log.info("########## accessToken, refreshToken 헤더에 넣어서 반환 완료 ##########");
  }

  public void sendAccessToken(HttpServletResponse response, String accessToken) {
    response.setStatus(HttpServletResponse.SC_OK);
    setAccessTokenHeader(response, accessToken);
    log.info("########## accessToken 헤더에 넣어서 반환 완료 ##########");
  }

  /** accessToken 헤더 설정 */
  public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
    response.setHeader(ACCESS_HEADER, accessToken);
  }

  /** refreshToken 헤더 설정 */
  public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
    response.setHeader(REFRESH_HEADER, refreshToken);
  }

  /** header에서 refreshToken 추출 토큰 앞에 붙은 Bearer 문자열 삭제 후 리턴 */
  public Optional<String> extractRefreshToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(REFRESH_HEADER))
        .filter(refreshToken -> refreshToken.startsWith(BEARER_PREFIX))
        .map(refreshToken -> refreshToken.replace(BEARER_PREFIX, ""));
  }

  /** header에서 accessToken 추출 토큰 앞에 붙은 Bearer 문자열 삭제 후 리턴 */
  public Optional<String> extractAccessToken(HttpServletRequest request) {
    return Optional.ofNullable(request.getHeader(ACCESS_HEADER))
        .filter(accessToken -> accessToken.startsWith(BEARER_PREFIX))
        .map(accessToken -> accessToken.replace(BEARER_PREFIX, ""));
  }

  public String extractAccessTokenAsString(HttpServletRequest request) {
    String header = request.getHeader(ACCESS_HEADER);
    if (header.startsWith(BEARER_PREFIX)) {
      return header.replace(BEARER_PREFIX, "");
    }
    return null;
  }

  public String extractRefreshTokenAsString(HttpServletRequest request) {
    String header = request.getHeader(REFRESH_HEADER);
    if (header.startsWith(BEARER_PREFIX)) {
      return header.replace(BEARER_PREFIX, "");
    }
    return null;
  }

  public boolean isAccessTokenExist(HttpServletRequest request) {
    return request.getHeader(ACCESS_HEADER) != null;
  }

  public boolean isRefreshTokenExist(HttpServletRequest request) {
    return request.getHeader(REFRESH_HEADER) != null;
  }

  public Optional<String> extractEmailClaim(String accessToken) {
    try {
      DecodedJWT decodedJWT = getVerifiedJWT(accessToken);
      return Optional.ofNullable(decodedJWT.getClaim(EMAIL_CLAIM).asString());
    } catch (Exception e) {
      log.error("%%%%%%%%%% 엑세스 토큰이 유효하지 않습니다 %%%%%%%%%%");
      return Optional.empty();
    }
  }

  public Long extractMemberIdClaim(String accessToken) {
    try {
      DecodedJWT decodedJWT = getVerifiedJWT(accessToken);
      return decodedJWT.getClaim(MEMBER_ID_CLAIM).asLong();
    } catch (Exception e) {
      log.error("%%%%%%%%%% 엑세스 토큰이 유효하지 않습니다. 에러 메시지: {} %%%%%%%%%%", e.getMessage());
      e.printStackTrace();
      throw e;
    }
  }

  public Long extractMemberIdClaimFromExpiredToken(String accessToken)
      throws JsonProcessingException {
    Base64.Decoder decoder = Base64.getUrlDecoder();
    String[] parts = accessToken.split("\\.");
    String jwtPayload = new String(decoder.decode(parts[1]));

    Map<String, Object> jwtPayloadMap = objectMapper.readValue(jwtPayload, Map.class);

    return Long.valueOf(jwtPayloadMap.get(MEMBER_ID_CLAIM).toString());
  }

  public DecodedJWT getVerifiedJWT(String jwtToken) {
    return JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(jwtToken);
  }

  public boolean isTokenValid(String token) {
    try {
      getVerifiedJWT(token);
      return true;
    } catch (Exception e) {
      log.error("%%%%%%%%%% 유효하지 않은 토큰 {} %%%%%%%%%%", e.getMessage());
      return false;
    }
  }

  public boolean isTokenExpired(String token) {
    try {
      DecodedJWT decodedJWT = getVerifiedJWT(token);
      return decodedJWT.getExpiresAt().before(new Date());
    } catch (TokenExpiredException e) {
      return true;
    }
  }

  @Transactional
  public void updateRefreshTokenByMemberId(Long memberId, String refreshToken) {
    memberRepository
        .findById(memberId)
        .ifPresent(
            member -> {
              member.updateRefreshToken(refreshToken);
              memberRepository.saveAndFlush(member);
            });
  }
}
