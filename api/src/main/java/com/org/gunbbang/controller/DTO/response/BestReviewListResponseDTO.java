package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BestReviewListResponseDTO extends BaseBakeryResponseDTO {
    private Long reviewCount;
    private String reviewText;
    private String firstMaxRecommendKeyword;
    private String secondMaxRecommendKeyword;

    public BestReviewListResponseDTO(
            Long bakeryId,
            String bakeryName,
            String bakeryPicture,
            Boolean isHACCP,
            Boolean isVegan,
            Boolean isNonGMO,
            String firstNearStation,
            String secondNearStation,
            Boolean isBooked,
            Long bookMarkCount,
            Long reviewCount,
            String reviewText,
            String firstMaxRecommendKeyword,
            String secondMaxRecommendKeyword
    ) {
        super(bakeryId, bakeryName, bakeryPicture, isHACCP, isVegan,
                isNonGMO, firstNearStation, secondNearStation, isBooked, bookMarkCount);
        this.reviewCount = reviewCount;
        this.reviewText = reviewText;
        this.firstMaxRecommendKeyword = firstMaxRecommendKeyword;
        this.secondMaxRecommendKeyword = secondMaxRecommendKeyword;
    }
}
