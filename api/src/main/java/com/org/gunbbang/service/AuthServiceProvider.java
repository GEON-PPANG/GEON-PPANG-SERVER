package com.org.gunbbang.service;

import com.org.gunbbang.PlatformType;
import java.util.HashMap;
import java.util.Map;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthServiceProvider {
  private static final Map<PlatformType, AuthService> authSerivceMap = new HashMap<>();

  private final KakaoAuthService kakaoAuthService;
  private final AppleAuthService appleAuthService;
  private final NativeAuthService nativeAuthService;

  @PostConstruct
  void initialAuthServiceMap() {
    authSerivceMap.put(PlatformType.KAKAO, kakaoAuthService);
    authSerivceMap.put(PlatformType.APPLE, appleAuthService);
    authSerivceMap.put(PlatformType.NONE, nativeAuthService);
  }

  public AuthService getAuthService(PlatformType platformType) {
    return authSerivceMap.get(platformType);
  }
}
