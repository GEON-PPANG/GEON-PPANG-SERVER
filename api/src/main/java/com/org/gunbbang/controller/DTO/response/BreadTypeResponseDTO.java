package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class BreadTypeResponseDTO {

  private Long breadTypeId;

  /** 글루텐프리, 비건빵, 넛프리, 저당 및 무설탕 */
  private String breadTypeName;

  private boolean isGlutenFree; // 글루텐프리

  private boolean isVegan; // 비건빵

  private boolean isNutFree; // 넛프리

  private boolean isSugarFree; // 저당 및 무설탕

  public boolean getIsGlutenFree() {
    return isGlutenFree;
  }

  public boolean getIsVegan() {
    return isVegan;
  }

  public boolean getIsNutFree() {
    return isNutFree;
  }

  public boolean getIsSugarFree() {
    return isSugarFree;
  }
}
