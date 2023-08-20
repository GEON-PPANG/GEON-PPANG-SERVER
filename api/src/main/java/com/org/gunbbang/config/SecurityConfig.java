package com.org.gunbbang.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.jwt.filter.JwtAuthenticationProcessingFilter;
import com.org.gunbbang.jwt.filter.JwtExceptionFilter;
import com.org.gunbbang.jwt.service.JwtService;
import com.org.gunbbang.login.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.org.gunbbang.login.handler.LoginFailureHandler;
import com.org.gunbbang.login.handler.LoginSuccessHandler;
import com.org.gunbbang.login.service.CustomUserDetailsService;
import com.org.gunbbang.repository.MemberRepository;
import javax.servlet.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
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
        .antMatchers(
            "/",
            "/css/**",
            "/images/**",
            "/js/**",
            "/favicon.ico",
            "/h2-console/**",
            "/health",
            "/profile",
            "/auth/signup",
            "/validation/nickname",
            "/validation/email",
            "/profile",
            "/actuator/health")
        .permitAll()
        .and()

        // 필터 순서: JwtExceptionFilter -> JwtAuthenticationProcessingFilter -> LogoutFilter ->
        // CustomJsonUsernamePasswordAuthenticationFilter
        .addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class)
        .addFilterBefore(
            jwtAuthenticationProcessingFilter(),
            CustomJsonUsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter(), JwtAuthenticationProcessingFilter.class);

    return http.build();
  }

  @Bean
  public CustomJsonUsernamePasswordAuthenticationFilter
      customJsonUsernamePasswordAuthenticationFilter() {
    CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter =
        new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
    customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(
        authenticationManager());
    customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(
        loginSuccessHandler());
    customJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(
        loginFailureHandler());
    return customJsonUsernamePasswordAuthenticationFilter;
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
    return new LoginSuccessHandler(jwtService, memberRepository);
  }

  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler();
  }

  @Bean
  public Filter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
  }

  @Bean
  public JwtExceptionFilter jwtExceptionFilter() {
    return new JwtExceptionFilter(handlerExceptionResolver);
  }
}
