package com.org.gunbbang.controller.DTO.response.BaseDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class BaseBakeryResponseDTO {
  private Long bakeryId;
  private String bakeryName;
  private String bakeryPicture;
  private boolean isHACCP;
  private boolean isVegan;
  private boolean isNonGMO;
  private String firstNearStation;
  private String secondNearStation;

  public boolean getIsHACCP() {
    return isHACCP;
  }

  public boolean getIsVegan() {
    return isVegan;
  }

  public boolean getIsNonGMO() {
    return isNonGMO;
  }
}
