package com.org.gunbbang.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    private Long bookmarkCount;

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
        switch(keyword){
            case "맛있어요":
                this.keywordDeliciousCount++;
                break;
            case "친절해요":
                this.keywordKindCount++;
                break;
            case "특별한 메뉴":
                this.keywordSpecialCount++;
                break;
            case "제로 웨이스트":
                this.keywordZeroWasteCount++;
                break;
            default:
        }
    }

    public Long updateBookMarkCount(boolean isAddingBookMark) {
        if (isAddingBookMark) {
            this.bookmarkCount += 1;
            return this.bookmarkCount;
        }
        this.bookmarkCount -= 1;
        return this.bookmarkCount;
    }
}


