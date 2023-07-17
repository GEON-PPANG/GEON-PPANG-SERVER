package com.org.gunbbang.controller.DTO.response.BaseDTO;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder
public class BaseBakeryResponseDTO {
    private Long bakeryId;
    private String bakeryName;
    private String bakeryPicture;
    private Boolean isHACCP;
    private Boolean isVegan;
    private Boolean isNonGMO;
    private String firstNearStation;
    private String secondNearStation;
    private Boolean isBookMarked;
    private Long bookMarkCount;

    public BaseBakeryResponseDTO (
            Long bakeryId,
            String bakeryName,
            String bakeryPicture,
            Boolean isHACCP,
            Boolean isVegan,
            Boolean isNonGMO,
            String firstNearStation,
            String secondNearStation,
            Boolean isBookMarkeded,
            Long bookMarkCount
    ) {
         this.bakeryId = bakeryId;
         this.bakeryName = bakeryName;
         this.bakeryPicture = bakeryPicture;
         this.isHACCP = isHACCP;
         this.isVegan = isVegan;
         this.isNonGMO = isNonGMO;
         this.firstNearStation = firstNearStation;
         this.secondNearStation = secondNearStation;
         this.isBookMarked = isBookMarkeded;
         this.bookMarkCount = bookMarkCount;
    }

    public BaseBakeryResponseDTO(Long bakeryId, String bakeryName, String bakeryPicture, Boolean isHACCP, Boolean isVegan, Boolean isNonGMO, String firstNearStation, String secondNearStation) {
        this.bakeryId = bakeryId;
        this.bakeryName = bakeryName;
        this.bakeryPicture = bakeryPicture;
        this.isHACCP = isHACCP;
        this.isVegan = isVegan;
        this.isNonGMO = isNonGMO;
        this.firstNearStation = firstNearStation;
        this.secondNearStation = secondNearStation;
    }
}
