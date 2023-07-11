package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BakeryDetailResponseDto {
    private Long bakeryId;
    private String bakeryName;
    private String bakeryPicture;
    private Boolean isHACCP;
    private Boolean isVegan;
    private Boolean isNonGMO;
    private BreadTypeResponseDto breadTypeResponseDto;
    private String firstNearStation;
    private String secondNearStation;
    private Boolean isBooked;
    private int bookmarkCount;
    private String homepage;
    private String openingTime;
    private String closedDay;
    private String phoneNumber;
    private List<MenuResponseDto> menuList;

    @Builder
    public BakeryDetailResponseDto(Long bakeryId, String bakeryName, String bakeryPicture, Boolean isHACCP, Boolean isVegan, Boolean isNonGMO, BreadTypeResponseDto breadTypeResponseDto, String firstNearStation, String secondNearStation, Boolean isBooked, int bookmarkCount, String homepage, String openingTime, String closedDay, String phoneNumber, List<MenuResponseDto> menuList) {
        this.bakeryId = bakeryId;
        this.bakeryName = bakeryName;
        this.bakeryPicture = bakeryPicture;
        this.isHACCP = isHACCP;
        this.isVegan = isVegan;
        this.isNonGMO = isNonGMO;
        this.breadTypeResponseDto = breadTypeResponseDto;
        this.firstNearStation = firstNearStation;
        this.secondNearStation = secondNearStation;
        this.isBooked = isBooked;
        this.bookmarkCount = bookmarkCount;
        this.homepage = homepage;
        this.openingTime = openingTime;
        this.closedDay = closedDay;
        this.phoneNumber = phoneNumber;
        this.menuList = menuList;
    }
}
