package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTOV2;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BakeryListResponseDTOV2 {

  private BaseBakeryResponseDTOV2 baseBakeryResponseDTOV2;
  private BreadTypeResponseDTO breadType;
  private long reviewCount;
  private boolean isBookMarked;

  @Builder
  public BakeryListResponseDTOV2(
      BreadTypeResponseDTO breadType,
      BaseBakeryResponseDTOV2 baseBakeryResponseDTOV2,
      long reviewCount,
      boolean isBookMarked) {
    this.reviewCount = reviewCount;
    this.breadType = breadType;
    this.baseBakeryResponseDTOV2 = baseBakeryResponseDTOV2;
    this.isBookMarked = isBookMarked;
  }

  public boolean getIsBookMarked() {
    return isBookMarked;
  }
}
