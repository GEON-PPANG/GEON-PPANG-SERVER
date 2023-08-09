package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BreadTypeResponseDTO {

  private Long breadTypeId;

  /** 글루텐프리, 비건빵, 넛프리, 저당 및 무설탕 */
  private String breadTypeName;

  private Boolean isGlutenFree; // 글루텐프리

  private Boolean isVegan; // 비건빵

  private Boolean isNutFree; // 넛프리

  private Boolean isSugarFree; // 저당 및 무설탕

  @Builder
  public BreadTypeResponseDTO(
      Long breadTypeId,
      String breadTypeName,
      boolean isGlutenFree,
      boolean isVegan,
      boolean isNutFree,
      boolean isSugarFree) {
    this.breadTypeId = breadTypeId;
    this.breadTypeName = breadTypeName;
    this.isGlutenFree = isGlutenFree;
    this.isVegan = isVegan;
    this.isNutFree = isNutFree;
    this.isSugarFree = isSugarFree;
  }
}
