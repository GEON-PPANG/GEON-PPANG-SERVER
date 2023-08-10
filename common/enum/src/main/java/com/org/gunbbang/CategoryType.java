package com.org.gunbbang;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum CategoryType {
  HARD_BREAD("하드빵류"),
  DESSERT("디저트류"),
  BRUNCH("브런치류");

  private final String name;
}
