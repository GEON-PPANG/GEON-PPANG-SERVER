package com.org.gunbbang.login;

import com.org.gunbbang.PlatformType;
import com.org.gunbbang.login.service.AuthService;
import com.org.gunbbang.login.service.KakaoAuthService;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public
class AuthServiceProvider { // TODO: AuthService으로 이름을 변경하고 MemberService 내에 구현되어있는 회원가입/회원탈퇴/로그아웃
  // 등등을 빼는건 어떤지?
  private static final Map<PlatformType, AuthService> authSerivceMap = new HashMap<>();

  private final KakaoAuthService kakaoAuthService;

  @PostConstruct
  void initalAuthServiceMap() {
    authSerivceMap.put(PlatformType.KAKAO, kakaoAuthService);
  }

  public AuthService getAuthService(PlatformType platformType) {
    return authSerivceMap.get(platformType);
  }
}
