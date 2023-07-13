package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseReviewResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class ReviewDetailResponseDto extends BaseReviewResponseDto {
    @NotNull
    Boolean isLike;

    public ReviewDetailResponseDto(Long reviewId, List<RecommendKeywordResponseDto> recommendKeywordList, String reviewText, Boolean isLike) {
        super(reviewId, recommendKeywordList, reviewText);
        this.isLike = isLike;
    }

}
