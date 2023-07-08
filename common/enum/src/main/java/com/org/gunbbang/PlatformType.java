package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PlatformType {

    KAKAO,
    APPLE,
    NAVER,
    GOOGLE,
    NONE // 자체로그인

}
