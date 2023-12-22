package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class BreadTypeResponseDTO {

  private boolean isGlutenFree; // 글루텐프리

  private boolean isVegan; // 비건빵

  private boolean isNutFree; // 넛프리

  private boolean isSugarFree; // 저당 및 무설탕
}
