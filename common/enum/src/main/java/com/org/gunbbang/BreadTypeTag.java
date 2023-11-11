package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BreadTypeTag {
  GLUTEN_FREE("글루텐프리"),
  VEGAN("비건"),
  NUT_FREE("넛프리"),
  SUGAR_FREE("대체당");

  private final String desc;
}
