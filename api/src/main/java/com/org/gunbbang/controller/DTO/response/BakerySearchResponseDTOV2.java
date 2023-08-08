package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakerySearchResponseDTOV2 {
    private int resultCount;
    private List<BakeryListResponseDTOV2> bakeryList;

    public BakerySearchResponseDTOV2(int resultCount, List<BakeryListResponseDTOV2> bakeryList) {
        this.resultCount = resultCount;
        this.bakeryList = bakeryList;
    }

    public static BakerySearchResponseDTOV2 getEmptyBakerySearchResponseDTO() {
        return new BakerySearchResponseDTOV2(
                0,
                Collections.EMPTY_LIST
        );
    }
}
