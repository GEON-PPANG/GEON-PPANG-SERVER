package com.org.gunbbang.jwt.filter;

import java.io.IOException;
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
