package com.org.gunbbang.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import com.org.gunbbang.common.RecommendKeywordType;

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

    @NotNull
    private String bakeryName;

    private String homepage;

    private String firstNearStation;

    private String secondNearStation;

    private String openingHours;

    private String closedDay;

    private String phoneNumber;

    @ColumnDefault("false")
    @Column(name = "is_haccp")
    private Boolean isHACCP;

    @ColumnDefault("false")
    private Boolean isVegan;

    @ColumnDefault("false")
    private Boolean isNonGMO;

    @NotNull
    private String bakeryPicture;

    @NotNull
    private String state;

    @NotNull
    private String city;

    @NotNull
    private String town;

    @NotNull
    private String addressRest;

    @ColumnDefault("0")
    private Long bookMarkCount;

    @ColumnDefault("0")
    private Long reviewCount;

    @ColumnDefault("0")
    private Long keywordDeliciousCount;

    @ColumnDefault("0")
    private Long keywordKindCount;

    @ColumnDefault("0")
    private Long keywordSpecialCount;

    @ColumnDefault("0")
    private Long keywordZeroWasteCount;

    public void reviewCountChange(boolean status){
        if(status){
            this.reviewCount++;
        }
        else{
            this.reviewCount--;
        }
    }
    public void keywordCountChange(String keyword) {
        RecommendKeywordType keywordType = RecommendKeywordType.valueOf(keyword);
        switch(keywordType){
            case DELICIOUS:
                this.keywordDeliciousCount++;
                break;
            case KIND:
                this.keywordKindCount++;
                break;
            case SPECIAL_MENU:
                this.keywordSpecialCount++;
                break;
            case ZERO_WASTE:
                this.keywordZeroWasteCount++;
                break;
            default:
        }
    }

    public Long updateBookMarkCount(boolean isAddingBookMark) {
        if (isAddingBookMark) {
            this.bookMarkCount += 1;
            return this.bookMarkCount;
        }
        this.bookMarkCount -= 1;
        return this.bookMarkCount;
    }
}


