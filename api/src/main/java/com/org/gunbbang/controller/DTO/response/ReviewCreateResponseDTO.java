package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewCreateResponseDTO {
    Long reviewId;
    @Builder
    public ReviewCreateResponseDTO(Long reviewId) {
        this.reviewId = reviewId;
    }
}
