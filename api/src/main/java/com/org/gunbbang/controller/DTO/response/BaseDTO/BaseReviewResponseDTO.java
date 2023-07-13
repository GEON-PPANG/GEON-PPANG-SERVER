package com.org.gunbbang.controller.DTO.response.BaseDTO;

import com.org.gunbbang.controller.DTO.response.RecommendKeywordResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseReviewResponseDTO {
    @NotNull
    Long reviewId;
    List<RecommendKeywordResponseDTO> recommendKeywordList;
    @NotNull
    String reviewText;

    public BaseReviewResponseDTO(Long reviewId, List<RecommendKeywordResponseDTO> recommendKeywordList, String reviewText) {
        this.reviewId = reviewId;
        this.recommendKeywordList = recommendKeywordList;
        this.reviewText = reviewText;
    }
}
