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
public class ReviewResponseDto extends BaseReviewResponseDto {
    @NotNull
    String memberNickname;
    @NotNull
    String createdAt;

    public ReviewResponseDto(Long reviewId, String memberNickname, List<RecommendKeywordResponseDto> recommendKeywordList, String reviewText, String createdAt) {
        super(reviewId, recommendKeywordList, reviewText);
        this.memberNickname = memberNickname;
        this.createdAt = createdAt;
    }
}
