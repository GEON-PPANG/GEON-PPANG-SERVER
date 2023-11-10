package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  ROLE_GUEST("GUEST"), // 소셜로그인까지 완료했으나 닉네임 설정 뷰에서 이탈한 유저
  ROLE_MEMBER("MEMBER"), // 회원가입까지 완료한 건빵 유저
  ROLE_ADMIN("ADMIN");

  private final String desc;
}
