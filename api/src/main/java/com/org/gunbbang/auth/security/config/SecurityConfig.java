package com.org.gunbbang.auth.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.org.gunbbang.auth.jwt.service.JwtService;
import com.org.gunbbang.auth.security.filter.JsonUsernamePasswordAuthenticationFilter;
import com.org.gunbbang.auth.security.filter.JwtAuthenticationProcessingFilter;
import com.org.gunbbang.auth.security.filter.JwtExceptionFilter;
import com.org.gunbbang.auth.security.handler.*;
import com.org.gunbbang.auth.security.service.CustomUserDetailsService;
import com.org.gunbbang.repository.MemberRepository;
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
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.filter.OncePerRequestFilter;
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
    http
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/favicon.ico", "/h2-console/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/member/nickname").hasRole("GUEST")
                    .requestMatchers(HttpMethod.GET, "/member/nickname").hasRole("MEMBER")
                    .requestMatchers(
                            "/reviews/{bakeryId}",
                            "/reviews/{reviewId}",
                            "/member/reviews",
                            "/bookMarks/{bakeryId}",
                            "/report/review/{reviewId}",
                            "/member/withdraw",
                            "/member",
                            "/member/bookMarks",
                            "/member/types",
                            "/auth/withdraw").hasRole("MEMBER")
                    .anyRequest().permitAll()
            )
            .exceptionHandling(exceptionHandling -> exceptionHandling
                    .accessDeniedHandler(customAccessDeniedHandler())
                    .authenticationEntryPoint(customAuthenticationEntryPoint())
            )
            .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .addLogoutHandler(customlogoutHandler())
                    .logoutSuccessHandler(customLogoutSuccessHandler())
            );


    http.addFilterBefore(jwtAuthenticationProcessingFilter(), LogoutFilter.class);
    http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterBefore(jwtExceptionFilter(), JwtAuthenticationProcessingFilter.class);

    return http.build();
  }

  @Bean
  public JsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() throws Exception {
    JsonUsernamePasswordAuthenticationFilter filter = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
    filter.setAuthenticationManager(authenticationManager());
    filter.setAuthenticationSuccessHandler(loginSuccessHandler());
    filter.setAuthenticationFailureHandler(loginFailureHandler());
    return filter;
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(customUserDetailsService);
    return new ProviderManager(provider);  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AuthenticationSuccessHandler loginSuccessHandler() {
    return new LoginSuccessHandler(jwtService, objectMapper);
  }

  @Bean
  public AuthenticationFailureHandler loginFailureHandler() {
    return new LoginFailureHandler(objectMapper);
  }

  @Bean
  public OncePerRequestFilter jwtAuthenticationProcessingFilter() {
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
