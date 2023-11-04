package com.org.gunbbang.auth.security.filter;

import javax.servlet.FilterChain;
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
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error(
          "%%%%%%%%%% JwtAuthenticationProcessingFilter에서 에러 발생. 에러 클래스 이름: {} 에러 메시지 이름: {}"
              + " %%%%%%%%%% ",
          e.getClass().getName(), e.getMessage());
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));
    }
  }
}
