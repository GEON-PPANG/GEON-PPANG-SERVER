package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewListResponseDTO {
    @NotNull
    Float tastePercent;
    @NotNull
    Float specialPercent;
    @NotNull
    Float kindPercent;
    @NotNull
    Float zeroPercent;
    @NotNull
    int totalReviewCount;
    List<ReviewResponseDTO> reviewList;

    @Builder
    public ReviewListResponseDTO(Float tastePercent, Float specialPercent, Float kindPercent, Float zeroPercent, int totalReviewCount, List<ReviewResponseDTO> reviewList) {
        this.tastePercent = tastePercent;
        this.specialPercent = specialPercent;
        this.kindPercent = kindPercent;
        this.zeroPercent = zeroPercent;
        this.totalReviewCount = totalReviewCount;
        this.reviewList = reviewList;
    }
}
