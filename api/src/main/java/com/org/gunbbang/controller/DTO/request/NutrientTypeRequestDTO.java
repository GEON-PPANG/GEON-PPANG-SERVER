package com.org.gunbbang.controller.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class NutrientTypeRequestDTO {
    @NotNull
    private Boolean isNutrientOpen;
    @NotNull
    private Boolean isIngredientOpen;
    @NotNull
    private Boolean isNotOpen;

}
