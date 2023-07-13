package com.org.gunbbang.controller.DTO.response.BaseDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseBakeryListResponseDTO {
    private Long bakeryId;
    private String bakeryName;
    private String bakeryPicture;
    private Boolean isHACCP;
    private Boolean isVegan;
    private Boolean isNonGMO;
    private String firstNearStation;
    private String secondNearStation;
    private Boolean isBooked;
    private int bookmarkCount;

    public BaseBakeryListResponseDTO (
            Long bakeryId,
            String bakeryName,
            String bakeryPicture,
            Boolean isHACCP,
            Boolean isVegan,
            Boolean isNonGMO,
            String firstNearStation,
            String secondNearStation,
            Boolean isBooked,
            int bookmarkCount
    ) {
         this.bakeryId = bakeryId;
         this.bakeryName = bakeryName;
         this.bakeryPicture = bakeryPicture;
         this.isHACCP = isHACCP;
         this.isVegan = isVegan;
         this.isNonGMO = isNonGMO;
         this.firstNearStation = firstNearStation;
         this.secondNearStation = secondNearStation;
         this.isBooked = isBooked;
         this.bookmarkCount = bookmarkCount;
    }
}
