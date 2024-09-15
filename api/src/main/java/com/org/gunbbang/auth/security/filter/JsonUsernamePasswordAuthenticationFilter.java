package com.org.gunbbang.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

/** 로그인 요청이 왔을 때 해당 유저가 유효한 유저인지 인증 처리하는 커스텀 필터 */
@Slf4j
public class JsonUsernamePasswordAuthenticationFilter
    extends AbstractAuthenticationProcessingFilter {
  private static final String DEFAULT_LOGIN_REQUEST_URL = "/auth/login";
  private static final String HTTP_METHOD = "POST";
  private static final String CONTENT_TYPE = "application/json";
  private static final String CONTENT_TYPE_UTF_8 = "application/json; charset=utf-8";
  private static final String USERNAME_KEY = "email";
  private static final String PASSWORD_KEY = "password";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // 로그인 url로 온 요청에 매칭됨

  private final ObjectMapper objectMapper;

  public JsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  /** usernamePasswordAuthenticationToken 생성 후 AuthenticationManager에 전달하여 인증 처리 시도 */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {

    // request ContentType이 application/json이 아닌 경우 에러
    if (request.getContentType() == null
        || (!request.getContentType().equals(CONTENT_TYPE)
            && !request.getContentType().equals(CONTENT_TYPE_UTF_8))) {
      log.error(
          "%%%%%%%%%% Authentication Content-Type not supported: {} %%%%%%%%%%",
          request.getContentType());
      throw new AuthenticationServiceException(
          "Authentication Content-Type not supported: " + request.getContentType());
    }

    String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

    Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

    String email = usernamePasswordMap.get(USERNAME_KEY);
    String password = usernamePasswordMap.get(PASSWORD_KEY);

    // request 요청에서 받은 email, password로 usernamePasswordAuthenticationToken 생성
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);

    // usernamePasswordAuthenticationToken을 AuthenticationManager에 전달
    // 성공 시 완전한 Authentication(usernamePasswordAuthenticationToken 타입) 반환
    return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
  }
}
