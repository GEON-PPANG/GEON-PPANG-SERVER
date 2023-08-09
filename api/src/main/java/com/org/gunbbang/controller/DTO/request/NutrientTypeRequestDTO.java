package com.org.gunbbang.controller.DTO.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class NutrientTypeRequestDTO {
  @NotNull private Boolean isNutrientOpen;
  @NotNull private Boolean isIngredientOpen;
  @NotNull private Boolean isNotOpen;

  @Override
  public String toString() {
    return "NutrientTypeRequestDTO{"
        + "isNutrientOpen="
        + isNutrientOpen
        + ", isIngredientOpen="
        + isIngredientOpen
        + ", isNotOpen="
        + isNotOpen
        + '}';
  }
}
