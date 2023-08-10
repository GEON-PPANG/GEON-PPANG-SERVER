package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakeryListResponseDTO extends BaseBakeryResponseDTO {
  private long reviewCount;
  private BreadTypeResponseDTO breadType;
  private boolean isBookMarked;
  private long bookMarkCount;

  public boolean getIsBookMarked() {
    return isBookMarked;
  }
}
