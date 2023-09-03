package com.org.gunbbang.service;

import com.org.gunbbang.PlatformType;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
// TODO: AuthService으로 이름을 변경하고 MemberService 내에 구현되어있는 회원가입/회원탈퇴/로그아웃 등등을 빼는건 어떤지?
public class AuthServiceProvider {
  private static final Map<PlatformType, AuthService> authSerivceMap = new HashMap<>();

  private final KakaoAuthService kakaoAuthService;
  private final AppleAuthService appleAuthService;
  private final NativeAuthService nativeAuthService;

  @PostConstruct
  void initalAuthServiceMap() {
    authSerivceMap.put(PlatformType.KAKAO, kakaoAuthService);
    authSerivceMap.put(PlatformType.APPLE, appleAuthService);
    authSerivceMap.put(PlatformType.NONE, nativeAuthService);
  }

  public AuthService getAuthService(PlatformType platformType) {
    return authSerivceMap.get(platformType);
  }
}
