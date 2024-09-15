package com.org.gunbbang.controller.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BreadTypeRequestDTO {
  @NotNull private boolean isGlutenFree;
  @NotNull private boolean isVegan;
  @NotNull private boolean isNutFree;
  @NotNull private boolean isSugarFree;

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
