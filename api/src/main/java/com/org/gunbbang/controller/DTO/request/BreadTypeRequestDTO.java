package com.org.gunbbang.controller.DTO.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BreadTypeRequestDTO {
  @NotNull private Boolean isGlutenFree;
  @NotNull private Boolean isVegan;
  @NotNull private Boolean isNutFree;
  @NotNull private Boolean isSugarFree;

  @Override
  public String toString() {
    return "BreadTypeRequestDTO{"
        + "isGlutenFree="
        + isGlutenFree
        + ", isVegan="
        + isVegan
        + ", isNutFree="
        + isNutFree
        + ", isSugarFree="
        + isSugarFree
        + '}';
  }
}
