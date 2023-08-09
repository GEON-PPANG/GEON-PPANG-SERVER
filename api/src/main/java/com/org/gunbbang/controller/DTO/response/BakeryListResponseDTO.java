package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakeryListResponseDTO extends BaseBakeryResponseDTO {
  private Long reviewCount;
  private BreadTypeResponseDTO breadType;
  private Boolean isBookMarked;
  private Long bookMarkCount;
}
