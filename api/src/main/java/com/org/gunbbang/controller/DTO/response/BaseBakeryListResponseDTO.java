package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BaseBakeryListResponseDTO {
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
}
