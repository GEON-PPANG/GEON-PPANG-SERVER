package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseReviewResponseDTO;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class ReviewDetailResponseDTO extends BaseReviewResponseDTO {
  @NotNull Boolean isLike;

  public ReviewDetailResponseDTO(
      Long reviewId,
      List<RecommendKeywordResponseDTO> recommendKeywordList,
      String reviewText,
      Boolean isLike) {
    super(reviewId, recommendKeywordList, reviewText);
    this.isLike = isLike;
  }
}
