package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NutrientTypeResponseDTO {
  private Long nutrientTypeId;
  private String nutrientTypeName;
  private Boolean isNutrientOpen;
  private Boolean isIngredientOpen;
  private Boolean isNotOpen;
}
