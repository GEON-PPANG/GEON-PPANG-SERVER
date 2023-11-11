package com.org.gunbbang;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NutrientTypeTag {
  INGREDIENT_OPEN("원재료 공개"),
  NUTRIENT_OPEN("영양성분 공개"),
  NOT_OPEN("비공개");

  private final String desc;
}
