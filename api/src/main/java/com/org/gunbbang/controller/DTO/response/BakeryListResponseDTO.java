package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakeryListResponseDTO extends BaseBakeryResponseDTO {
  private long reviewCount;
  private List<BreadTypeResponseDTO> breadTypeList;
  private long bookMarkCount;
}
