package com.org.gunbbang.controller.DTO.response.BaseDTO;

import com.org.gunbbang.controller.DTO.response.RecommendKeywordResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseReviewResponseDto {
    @NotNull
    Long reviewId;
    List<RecommendKeywordResponseDto> recommendKeywordList;
    @NotNull
    String reviewText;

    public BaseReviewResponseDto(Long reviewId, String memberNickname, List<RecommendKeywordResponseDto> recommendKeywordList, String reviewText) {
        this.reviewId = reviewId;
        this.recommendKeywordList = recommendKeywordList;
        this.reviewText = reviewText;
    }
}
