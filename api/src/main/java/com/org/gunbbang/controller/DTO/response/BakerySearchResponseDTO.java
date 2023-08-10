package com.org.gunbbang.controller.DTO.response;

import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
public class BakerySearchResponseDTO {
  private int resultCount;
  private List<BakeryListResponseDTO> bakeryList;

  public static BakerySearchResponseDTO getEmptyBakerySearchResponseDTO() {
    return new BakerySearchResponseDTO(0, Collections.EMPTY_LIST);
  }
}
