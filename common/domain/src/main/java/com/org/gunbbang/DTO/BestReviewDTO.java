package com.org.gunbbang;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class BestReviewDTO {
  private final Long bakeryId;
  private final String bakeryName;
  private final String bakeryPicture;
  private final boolean isHACCP;
  private final boolean isVegan;
  private final boolean isNonGMO;
  private final String firstNearStation;
  private final String secondNearStation;
  private final long bookMarkCount;

  private final long reviewId;
  private final long reviewCount;
  private final String reviewText;

  private final Long keywordDeliciousCount;
  private final Long keywordKindCount;
  private final Long keywordSpecialCount;
  private final Long keywordZeroWasteCount;

  private final LocalDateTime createdAt;

  public BestReviewDTO(
      Long bakeryId,
      String bakeryName,
      String bakeryPicture,
      boolean isHACCP,
      boolean isVegan,
      boolean isNonGMO,
      String firstNearStation,
      String secondNearStation,
      long bookMarkCount,
      long reviewId,
      long reviewCount,
      String reviewText,
      long keywordDeliciousCount,
      long keywordKindCount,
      long keywordSpecialCount,
      long keywordZeroWasteCount,
      LocalDateTime createdAt) {
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

  public boolean getIsHACCP() {
    return isHACCP;
  }

  public boolean getIsVegan() {
    return isVegan;
  }

  public boolean getIsNonGMO() {
    return isNonGMO;
  }
}
