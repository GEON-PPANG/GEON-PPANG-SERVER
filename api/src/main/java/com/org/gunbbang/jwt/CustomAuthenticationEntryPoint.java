package com.org.gunbbang.jwt;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private HandlerExceptionResolver resolver;

  public CustomAuthenticationEntryPoint(
      @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
    this.resolver = resolver;
  }

  // 시큐리티 인증 관련 예외 처리
  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException, ServletException {
    System.out.println("commence 진입");
    //        throw new AuthenticationServiceException("ddd");
    //        resolver.resolveException(request, response, null, (Exception)
    // request.getAttribute("exception"));  // HandlerExceptionResolver에 에러 처리 위임
  }
}
