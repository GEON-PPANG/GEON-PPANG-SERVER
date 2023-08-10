package com.org.gunbbang.controller.DTO.response.BaseDTO;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseBakeryResponseDTOV2 {
  private Long bakeryId;
  private String bakeryName;
  private String bakeryPicture;
  private Boolean isHACCP;
  private Boolean isVegan;
  private Boolean isNonGMO;
  private String firstNearStation;
  private String secondNearStation;

  // TODO: 빌더+정팩메 같이사용하면 코드 변경이 더 줄어들 것 같은데 어떤지? 안그러면 변경 시 빌더 쓰는 곳에서 다 바꿔줘야함
  @Builder
  public BaseBakeryResponseDTOV2(
      Long bakeryId,
      String bakeryName,
      String bakeryPicture,
      Boolean isHACCP,
      Boolean isVegan,
      Boolean isNonGMO,
      String firstNearStation,
      String secondNearStation) {
    this.bakeryId = bakeryId;
    this.bakeryName = bakeryName;
    this.bakeryPicture = bakeryPicture;
    this.isHACCP = isHACCP;
    this.isVegan = isVegan;
    this.isNonGMO = isNonGMO;
    this.firstNearStation = firstNearStation;
    this.secondNearStation = secondNearStation;
  }
}
