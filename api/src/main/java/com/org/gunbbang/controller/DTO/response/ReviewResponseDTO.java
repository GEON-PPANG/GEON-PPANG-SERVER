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
public class ReviewResponseDTO extends BaseReviewResponseDTO {
  @NotNull String memberNickname;
  @NotNull String createdAt;

  public ReviewResponseDTO(
      Long reviewId,
      String memberNickname,
      List<RecommendKeywordResponseDTO> recommendKeywordList,
      String reviewText,
      String createdAt) {
    super(reviewId, recommendKeywordList, reviewText);
    this.memberNickname = memberNickname;
    this.createdAt = createdAt;
  }
}
