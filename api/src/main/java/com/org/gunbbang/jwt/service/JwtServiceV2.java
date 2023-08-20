package com.org.gunbbang.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceV2 {

  @Value("${jwt.secretKey}")
  private String jwtSecret;

  @PostConstruct
  protected void init() {
    jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes(StandardCharsets.UTF_8));
  }

  // JWT 토큰 발급
  public String issuedToken(String userId) {
    final Date now = new Date();

    // 클레임 생성
    final Claims claims =
        Jwts.claims()
            .setSubject("access_token")
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + 120 * 60 * 1000L));

    // private claim 등록
    claims.put("userId", userId);

    return Jwts.builder()
        .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
        .setClaims(claims)
        .signWith(getSigningKey())
        .compact();
  }

  private Key getSigningKey() {
    final byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // JWT 토큰 검증
  public boolean verifyToken(String token) {
    try {
      final Claims claims = getBody(token);
      return true;
    } catch (Exception e) {
      if (e instanceof ExpiredJwtException) {
        System.out.println("만료된 토큰");
      }
      return false;
    }
  }

  public void isTokenVerified(String token) {
    try {
      final Claims claims = getBody(token);
    } catch (Exception e) {
      System.out.println("에러 클래스: " + e.getClass().getName());
      System.out.println("에러 메시지: " + e.getMessage());

      if (e instanceof ExpiredJwtException) {
        System.out.println("만료된 토큰");
      }

      throw e;
    }
  }

  public Claims getBody(final String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // JWT 토큰 내용 확인
  public String getJwtContents(String token) {
    final Claims claims = getBody(token);
    return (String) claims.get("userId");
  }
}
