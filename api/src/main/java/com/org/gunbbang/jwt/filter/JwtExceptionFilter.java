package com.org.gunbbang.jwt.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

  private final HandlerExceptionResolver resolver;
  private static final String H2_PREFIX = "/h2-console"; // 로그인 요청은 필터에서 제외

  private static final List<String> WHITE_LIST =
      List.of(
          "/auth/signup",
          "/auth/login",
          "/health",
          "/profile",
          "/validation/nickname",
          "/validation/email",
          "/actuator/health",
          "/favicon.ico");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    System.out.println("path: " + path);
    return WHITE_LIST.contains(path) || path.startsWith(H2_PREFIX);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    log.info("JwtExceptionFilter 진입");

    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.info("JwtAuthenticationProcessingFilter에서 에러 발생. 에러 클래스 이름: " + e.getClass().getName());
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));
    }
  }
}
