package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseReviewResponseDTO;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class ReviewDetailResponseDTO extends BaseReviewResponseDTO {
  @NotNull boolean isLike;

  public ReviewDetailResponseDTO(
      Long reviewId,
      List<RecommendKeywordResponseDTO> recommendKeywordList,
      String reviewText,
      boolean isLike) {
    super(reviewId, recommendKeywordList, reviewText);
    this.isLike = isLike;
  }

  public boolean getIsLike() {
    return isLike;
  }
}
