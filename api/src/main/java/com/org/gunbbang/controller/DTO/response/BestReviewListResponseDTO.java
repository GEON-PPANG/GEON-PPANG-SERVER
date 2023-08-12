package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class BestReviewListResponseDTO extends BaseBakeryResponseDTO {
  private Long reviewCount;
  private String reviewText;
  private String firstMaxRecommendKeyword;
  private String secondMaxRecommendKeyword;
  private Long bookMarkCount;
}
