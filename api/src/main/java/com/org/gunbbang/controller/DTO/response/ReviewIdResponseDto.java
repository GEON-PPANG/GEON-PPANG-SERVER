package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewIdResponseDto {
    Long reviewId;

    @Builder
    public ReviewIdResponseDto(Long reviewId) {
        this.reviewId = reviewId;
    }
}
