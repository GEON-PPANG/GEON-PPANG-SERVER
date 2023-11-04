package com.org.gunbbang.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.auth.jwt.service.JwtService;
import com.org.gunbbang.auth.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.org.gunbbang.auth.security.filter.JwtAuthenticationProcessingFilter;
import com.org.gunbbang.auth.security.filter.JwtExceptionFilter;
import com.org.gunbbang.auth.security.handler.*;
import com.org.gunbbang.auth.security.service.CustomUserDetailsService;
import com.org.gunbbang.repository.MemberRepository;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final CustomUserDetailsService customUserDetailsService;
  private final JwtService jwtService;
  private final MemberRepository memberRepository;
  private final ObjectMapper objectMapper;

  @Qualifier("handlerExceptionResolver")
  private final HandlerExceptionResolver handlerExceptionResolver;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .formLogin()
        .disable() // FormLogin 사용안함
        .httpBasic()
        .disable() // httpBasic 사용안함
        .headers()
        .frameOptions()
        .disable()
        .and()

        // 세션 사용하지 않으므로 STATELESS로 설정
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .authorizeRequests()
        .antMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**")
        .permitAll()
        .antMatchers(
            HttpMethod.POST, "/member/nickname" // 소셜용 닉네임 변경 api
            )
        .hasRole("GUEST")
        .antMatchers(
            HttpMethod.GET, "/member/nickname" // 회원 닉네임 조회 api
            )
        .hasRole("MEMBER")
        .antMatchers(
            "/reviews/{bakeryId}", // 리뷰작성
            "/reviews/{reviewId}", // 내가 쓴 리뷰 상세보기
            "/member/reviews", // 내가 작성한 리뷰 목록
            "/bookMarks/{bakeryId}", // 북마크
            "/report/review/{reviewId}", // 리뷰신고
            "/member/withdraw", // 회원탈퇴
            "/auth/logout", // 로그아웃
            "/member", // 유저 정보 상세보기(마이페이지)
            "/member/bookMarks", // 북마크 목록 조회
            "/member/types" // 유져 필터 조회(이거 아요에서 한번 쓰지 않나?) 및 필터 변경
            )
        .hasRole("MEMBERRR")
        .and()
        .exceptionHandling()
        .accessDeniedHandler(customAccessDeniedHandler())
        .authenticationEntryPoint(customAuthenticationEntryPoint());

    // logout 구현
    http.logout()
        .logoutUrl("/auth/logout") // 로그아웃 URL 설정
        .addLogoutHandler(customlogoutHandler())
        .logoutSuccessHandler(customLogoutSuccessHandler());

    // 필터 순서: JwtExceptionFilter -> JwtAuthenticationProcessingFilter -> LogoutFilter
    // -> CustomJsonUsernamePasswordAuthenticationFilter
    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
        .addFilterBefore(
            jwtAuthenticationProcessingFilter(), JsonUsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter(), JwtAuthenticationProcessingFilter.class);

    return http.build();
  }

  @Bean
  public JsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
    JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter =
        new JsonUsernamePasswordAuthenticationFilter(objectMapper);
    jsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
    jsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
    jsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return jsonUsernamePasswordAuthenticationFilter;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(customUserDetailsService);
    return new ProviderManager(provider);
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public LoginSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtService, objectMapper);
  }

  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler(objectMapper);
  }

  @Bean
  public Filter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtService, memberRepository, objectMapper);
  }

  @Bean
  public JwtExceptionFilter jwtExceptionFilter() {
    return new JwtExceptionFilter(handlerExceptionResolver);
  }

  @Bean
  public CustomLogoutHandler customlogoutHandler() {
    return new CustomLogoutHandler(memberRepository);
  }

  @Bean
  public LogoutSuccessHandler customLogoutSuccessHandler() {
    return new LogoutSuccessHandler(objectMapper);
  }

  @Bean
  public AuthenticationEntryPoint customAuthenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint(objectMapper);
  }

  @Bean
  public AccessDeniedHandler customAccessDeniedHandler() {
    return new CustomAccessDeniedHandler(objectMapper);
  }
}
