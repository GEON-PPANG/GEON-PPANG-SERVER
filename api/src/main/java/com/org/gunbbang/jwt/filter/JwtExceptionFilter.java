package com.org.gunbbang.jwt.filter;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

  @Autowired
  @Qualifier("handlerExceptionResolver")
  private HandlerExceptionResolver resolver;

  private final ObjectMapper objectMapper;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    System.out.println("JwtExceptionFilter 진입");

    try {
      filterChain.doFilter(request, response);
    } catch (TokenExpiredException e) {
      // 만료 에러
      System.out.println("JwtExceptionFilter 내 TokenExpiredException");
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));
      throw new AuthenticationServiceException("ddd");

    } catch (SignatureVerificationException e) {

      // 변조 에러
      System.out.println("JwtExceptionFilter 내 SignatureVerificationException");
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));
      throw new AuthenticationServiceException("ddd");

    } catch (JWTVerificationException e) {
      // 형식, 길이 에러
      System.out.println("JwtExceptionFilter 내 JWTVerificationException");
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));

    } catch (Exception e) {
      System.out.println("JwtExceptionFilter 내 Exception");
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));
    }

    //        System.out.println("두번째 do filter");
    filterChain.doFilter(request, response); // 이거 없으면 ExceptionTranslationFilter 로그 안뜸
  }
}
