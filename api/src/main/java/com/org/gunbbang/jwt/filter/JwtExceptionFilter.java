package com.org.gunbbang.jwt.filter;

import com.org.gunbbang.support.slack.SlackSender;
import java.io.IOException;
import java.util.List;
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
  private static final String H2_PREFIX = "/h2-console"; // 로그인 요청은 필터에서 제외
  private final SlackSender slackSender;

  private static final List<String> WHITE_LIST =
      List.of(
          "/auth/signup",
          "/auth/login",
          "/health",
          "/profile",
          "/validation/nickname",
          "/validation/email",
          "/actuator/health",
          "/favicon.ico",
          "/oauth2/authorization/kakao",
          "login/oauth2/code/kakao");

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    String path = request.getRequestURI();
    return WHITE_LIST.contains(path) || path.startsWith(H2_PREFIX);
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      log.error(
          "%%%%%%%%%% JwtAuthenticationProcessingFilter에서 에러 발생. 에러 클래스 이름: {} 에러 메시지 이름: {}"
              + " %%%%%%%%%% ",
          e.getClass().getName(), e.getMessage());
      e.printStackTrace();
      slackSender.sendAlertWithMessage(e, request);
      request.setAttribute("exception", e);
      resolver.resolveException(
          request, response, null, (Exception) request.getAttribute("exception"));
    }
  }
}
