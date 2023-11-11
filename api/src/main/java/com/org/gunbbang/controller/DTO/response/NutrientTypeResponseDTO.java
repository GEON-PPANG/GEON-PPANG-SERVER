package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class NutrientTypeResponseDTO {
  private Long nutrientTypeId = 0L;
  private String nutrientTypeName = "";
  private boolean isNutrientOpen;
  private boolean isIngredientOpen;
  private boolean isNotOpen;

  public boolean getIsNutrientOpen() {
    return isNutrientOpen;
  }

  public boolean getIsIngredientOpen() {
    return isIngredientOpen;
  }

  public boolean getIsNotOpen() {
    return isNotOpen;
  }
}
