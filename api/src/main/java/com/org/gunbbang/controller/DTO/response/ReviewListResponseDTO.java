package com.org.gunbbang.controller.DTO.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewListResponseDTO {
  float deliciousPercent;
  float specialPercent;
  float kindPercent;
  float zeroWastePercent;
  long totalReviewCount;
  List<ReviewResponseDTO> reviewList;

  @Builder
  public ReviewListResponseDTO(
      float deliciousPercent,
      float specialPercent,
      float kindPercent,
      float zeroWastePercent,
      long totalReviewCount,
      List<ReviewResponseDTO> reviewList) {
    this.deliciousPercent = deliciousPercent;
    this.specialPercent = specialPercent;
    this.kindPercent = kindPercent;
    this.zeroWastePercent = zeroWastePercent;
    this.totalReviewCount = totalReviewCount;
    this.reviewList = reviewList;
  }
}
