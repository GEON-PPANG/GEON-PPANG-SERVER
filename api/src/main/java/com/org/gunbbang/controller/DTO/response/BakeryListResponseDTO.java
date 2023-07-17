package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakeryListResponseDTO extends BaseBakeryResponseDTO {
    private Long reviewCount;
    private BreadTypeResponseDTO breadType;

    public BakeryListResponseDTO(Long bakeryId, String bakeryName, String bakeryPicture, Boolean isHACCP, Boolean isVegan, Boolean isNonGMO, BreadTypeResponseDTO breadType, String firstNearStation, String secondNearStation, Boolean isBooked, Long bookMarkCount, Long reviewCount) {
        super(bakeryId, bakeryName, bakeryPicture, isHACCP, isVegan,
                isNonGMO, firstNearStation, secondNearStation, isBooked, bookMarkCount);
        this.reviewCount = reviewCount;
        this.breadType = breadType;
    }
}
