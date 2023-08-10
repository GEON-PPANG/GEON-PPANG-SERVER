package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  GUEST("ROLE_GUEST"),
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String desc;
}
