package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewResponseDto {
    @NotNull
    Long reviewId;
    @NotNull
    String memberNickname;
    List<RecommendKeywordResponseDto> recommendKeywordList;
    @NotNull
    String reviewText;
    @NotNull
    String createdAt;

    @Builder
    public ReviewResponseDto(Long reviewId, String memberNickname, List<RecommendKeywordResponseDto> recommendKeywordList, String reviewText, String createdAt) {
        this.reviewId = reviewId;
        this.memberNickname = memberNickname;
        this.recommendKeywordList = recommendKeywordList;
        this.reviewText = reviewText;
        this.createdAt = createdAt;
    }
}
