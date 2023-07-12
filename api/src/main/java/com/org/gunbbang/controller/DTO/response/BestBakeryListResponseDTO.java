package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryListResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@NoArgsConstructor
@SuperBuilder
public class BestBakeryListResponseDTO extends BaseBakeryListResponseDTO {
    private int reviewCount;

    public BestBakeryListResponseDTO(
            Long bakeryId,
            String bakeryName,
            String bakeryPicture,
            Boolean isHACCP,
            Boolean isVegan,
            Boolean isNonGMO,
            String firstNearStation,
            String secondNearStation,
            Boolean isBooked,
            int bookmarkCount,
            int reviewCount
    ) {
        super(bakeryId, bakeryName, bakeryPicture, isHACCP, isVegan,
                isNonGMO, firstNearStation, secondNearStation, isBooked, bookmarkCount);
        this.reviewCount = reviewCount;
    }
}

