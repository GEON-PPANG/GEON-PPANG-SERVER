package com.org.gunbbang;

import java.util.*;
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

  public static List<CategoryType> getCategoryTypeList(
      boolean isHard, boolean isDessert, boolean isBrunch) {
    List<CategoryType> categoryTypeList = new ArrayList<>();
    Map<CategoryType, Boolean> categoryMap = new HashMap<>();
    categoryMap.put(CategoryType.HARD_BREAD, isHard);
    categoryMap.put(CategoryType.DESSERT, isDessert);
    categoryMap.put(CategoryType.BRUNCH, isBrunch);
    for (Map.Entry<CategoryType, Boolean> entry : categoryMap.entrySet()) {
      if (entry.getValue()) {
        categoryTypeList.add(entry.getKey());
      }
    }
    if (categoryTypeList.isEmpty()) {
      categoryTypeList = Arrays.asList(CategoryType.values());
    }
    return categoryTypeList;
  }
}
