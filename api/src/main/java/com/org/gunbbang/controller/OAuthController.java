package com.org.gunbbang.controller;

import com.org.gunbbang.login.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OAuthController {

  private final CustomOAuth2UserService oAuth2UserDetailsService;

  @GetMapping("/login/oauth2/code/kakao")
  public String handleOAuthCallback(Authentication authentication) {
    System.out.println("진입함 ㅋㅋ");
    OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

    // OAuth2UserDetailsServiceImpl를 사용하여 사용자 정보 처리
    /// KakaoOAuth2UserInfo userDetails = oAuth2UserDetailsService.loadUser(oauthToken);

    // 로그인 성공 후 로직 처리
    // ...

    return "redirect:/profile";
  }
}
