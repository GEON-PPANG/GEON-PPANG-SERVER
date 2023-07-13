package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakeryDetailResponseDto extends BaseBakeryResponseDTO {
    private BreadTypeResponseDto breadType;
    private String homepage;
    private String address;
    private String openingTime;
    private String closedDay;
    private String phoneNumber;
    private List<MenuResponseDto> menuList;

    public BakeryDetailResponseDto(Long bakeryId, String bakeryName, String bakeryPicture, Boolean isHACCP, Boolean isVegan, Boolean isNonGMO, BreadTypeResponseDto breadType, String firstNearStation, String secondNearStation, Boolean isBooked, Long bookMarkCount, String homepage, String address, String openingTime, String closedDay, String phoneNumber, List<MenuResponseDto> menuList) {
        super(bakeryId, bakeryName, bakeryPicture, isHACCP, isVegan,
                isNonGMO, firstNearStation, secondNearStation, isBooked, bookMarkCount);
        this.breadType = breadType;
        this.homepage = homepage;
        this.address = address;
        this.openingTime = openingTime;
        this.closedDay = closedDay;
        this.phoneNumber = phoneNumber;
        this.menuList = menuList;
    }
}
