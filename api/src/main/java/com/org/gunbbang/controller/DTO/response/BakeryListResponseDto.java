package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakeryListResponseDto extends BaseBakeryResponseDTO {
    private BreadTypeResponseDto breadTypeResponseDto;

    public BakeryListResponseDto(Long bakeryId, String bakeryName, String bakeryPicture, Boolean isHACCP, Boolean isVegan, Boolean isNonGMO, BreadTypeResponseDto breadTypeResponseDto, String firstNearStation, String secondNearStation, Boolean isBooked, Long bookMarkCount) {
        super(bakeryId, bakeryName, bakeryPicture, isHACCP, isVegan,
                isNonGMO, firstNearStation, secondNearStation, isBooked, bookMarkCount);
        this.breadTypeResponseDto = breadTypeResponseDto;
    }
}
