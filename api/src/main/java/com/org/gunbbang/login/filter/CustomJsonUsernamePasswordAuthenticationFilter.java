package com.org.gunbbang.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

/**
 * 스프링 시큐리티 폼 기반 UsernamePasswordAuthenticationFilter 변형 커스텀 필터 로그인 요청이 왔을 때 해당 유저가 유효한 유저인지 인증 처리함
 */
public class CustomJsonUsernamePasswordAuthenticationFilter
    extends AbstractAuthenticationProcessingFilter {
  private static final String DEFAULT_LOGIN_REQUEST_URL = "/auth/login";
  private static final String HTTP_METHOD = "POST";
  private static final String CONTENT_TYPE = "application/json";
  private static final String USERNAME_KEY = "email";
  private static final String PASSWORD_KEY = "password";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // 로그인 url로 온 요청에 매칭된다

  private final ObjectMapper objectMapper;

  public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
    // 부모 객체인 AbstractAuthenticationProcessingFilter 생성
    // 생성자 매개변수로 DEFAULT_LOGIN_PATH_REQUEST_MATCHER(authentication이 필요한지 필요하지 않은지 결정하기 위해 사용되는
    // RequestMatcher)를 넘겨줌
    // 해당 request matcher로 지정된 url은 이 필터를 거침
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    this.objectMapper = objectMapper;
  }

  /**
   * attemptAuthentication 함수
   *
   * <p>UsernamePasswordAuthenticationFilter와 동일하게 UsernamePasswordAuthenticationToken 사용
   * UsernamePasswordAuthenticationFilter와 동일하게 AbstractAuthenticationProcessingFilter의
   * attemptAuthentication 오버라이딩해서 사용 1. StreamUtils를 통해 requestBody를 꺼냄 2.
   * objectMapper.readValue()로 json의 키, json의 value를 usernamePasswordMap으로 변환 3.
   * UsernamePasswordAuthenticationToken의 파라미터인 principal와 credentials에 대입
   *
   * <p>AbstractAuthenticationProcessingFilter(부모)의 getAuthenticationManager()로
   * AuthenticationManager 객체를 반환 받은 후 authenticate()의 파라미터로 UsernamePasswordAuthenticationToken 객체를
   * 넣고 인증 처리 (여기서 AuthenticationManager 객체는 ProviderManager -> SecurityConfig에서 설정, )
   */
  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {
    // request ContentType이 application/json이 아닌 경우 에러
    if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
      throw new AuthenticationServiceException(
          "Authentication Content-Type not supported: " + request.getContentType());
    }

    String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

    Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

    String email = usernamePasswordMap.get(USERNAME_KEY);
    String password = usernamePasswordMap.get(PASSWORD_KEY);

    // principal 과 credentials을 각각 email, password로 지정해서 usernamePasswordAuthenticationToken 생성
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
        new UsernamePasswordAuthenticationToken(email, password);

    // 생성한 usernamePasswordAuthenticationToken을 실질적인 인증 프로세스를 담당하는 AuthenticationManager 에게 전달
    // authenticate 설명: 전달받은 Authentication 인증 객체를 인증 처리하려는 시도. 성공할 경우 완전하게 채워진 Authentication 객체를
    // 반환
    return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
  }
}
