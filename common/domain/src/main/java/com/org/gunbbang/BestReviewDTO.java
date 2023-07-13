package com.org.gunbbang;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BestReviewDTO {
    private final Long bakeryId;
    private final String bakeryName;
    private final String bakeryPicture;
    private final Boolean isHACCP;
    private final Boolean isVegan;
    private final Boolean isNonGMO;
    private final String firstNearStation;
    private final String secondNearStation;
    private final Long bookMarkCount;

    private final Long reviewId;
    private final Long reviewCount;
    private final String reviewText;

    private final Long keywordDeliciousCount;
    private final Long keywordKindCount;
    private final Long keywordSpecialCount;
    private final Long keywordZeroWasteCount;

    private final LocalDateTime createdAt;

    public BestReviewDTO(Long bakeryId, String bakeryName, String bakeryPicture, Boolean isHACCP, Boolean isVegan, Boolean isNonGMO, String firstNearStation, String secondNearStation, Long bookMarkCount, Long reviewId, Long reviewCount, String reviewText, Long keywordDeliciousCount, Long keywordKindCount, Long keywordSpecialCount, Long keywordZeroWasteCount, LocalDateTime createdAt) {
        this.bakeryId = bakeryId;
        this.bakeryName = bakeryName;
        this.bakeryPicture = bakeryPicture;
        this.isHACCP = isHACCP;
        this.isVegan = isVegan;
        this.isNonGMO = isNonGMO;
        this.firstNearStation = firstNearStation;
        this.secondNearStation = secondNearStation;
        this.bookMarkCount = bookMarkCount;
        this.reviewId = reviewId;
        this.reviewCount = reviewCount;
        this.reviewText = reviewText;
        this.keywordDeliciousCount = keywordDeliciousCount;
        this.keywordKindCount = keywordKindCount;
        this.keywordSpecialCount = keywordSpecialCount;
        this.keywordZeroWasteCount = keywordZeroWasteCount;
        this.createdAt = createdAt;
    }
}
