package com.org.gunbbang.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
    private BreadType breadTypeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nutrient_type_id")
    @NotNull
    private NutrientType nutrientTypeId;

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
    private boolean isHACCP;

    @ColumnDefault("false")
    private boolean isVegan;

    @ColumnDefault("false")
    private boolean isNonGMO;

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
}


