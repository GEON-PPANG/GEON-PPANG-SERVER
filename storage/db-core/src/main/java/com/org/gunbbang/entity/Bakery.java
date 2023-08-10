package com.org.gunbbang.entity;

import com.org.gunbbang.common.RecommendKeywordType;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bakery {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bakery_id")
  private Long bakeryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bread_type_id")
  @NotNull
  private BreadType breadType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "nutrient_type_id")
  @NotNull
  private NutrientType nutrientType;

  @NotNull private String bakeryName;

  private String homepage;

  private String firstNearStation;

  private String secondNearStation;

  private String openingHours;

  private String closedDay;

  private String phoneNumber;

  @ColumnDefault("false")
  @Column(name = "is_haccp")
  private boolean isHACCP;

  @ColumnDefault("false")
  private boolean isVegan;

  @ColumnDefault("false")
  private boolean isNonGMO;

  @NotNull private String bakeryPicture;

  @NotNull private String state;

  @NotNull private String city;

  @NotNull private String town;

  @NotNull private String addressRest;

  @ColumnDefault("0")
  private long bookMarkCount;

  @ColumnDefault("0")
  private long reviewCount;

  @ColumnDefault("0")
  private long keywordDeliciousCount;

  @ColumnDefault("0")
  private long keywordKindCount;

  @ColumnDefault("0")
  private long keywordSpecialCount;

  @ColumnDefault("0")
  private long keywordZeroWasteCount;

  public void reviewCountChange(boolean status) {
    if (status) {
      this.reviewCount++;
    } else {
      this.reviewCount--;
    }
  }

  public void keywordCountChange(String keyword) {
    switch (keyword) {
      case RecommendKeywordType.DELICIOUS:
        this.keywordDeliciousCount++;
        break;
      case RecommendKeywordType.KIND:
        this.keywordKindCount++;
        break;
      case RecommendKeywordType.SPECIAL_MENU:
        this.keywordSpecialCount++;
        break;
      case RecommendKeywordType.ZERO_WASTE:
        this.keywordZeroWasteCount++;
        break;
      default:
    }
  }

  public long updateBookMarkCount(boolean isAddingBookMark) {
    if (isAddingBookMark) {
      this.bookMarkCount += 1;
      return this.bookMarkCount;
    }
    this.bookMarkCount -= 1;
    return this.bookMarkCount;
  }

  public boolean getIsHACCP() {
    return this.isHACCP;
  }

  public boolean getIsVegan() {
    return this.isVegan;
  }

  public boolean getIsNonGMO() {
    return this.isHACCP;
  }
}
