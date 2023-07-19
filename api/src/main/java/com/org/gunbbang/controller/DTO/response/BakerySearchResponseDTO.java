package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakerySearchResponseDTO {
    private int resultCount;
    private List<BakeryListResponseDTO> bakeryList;

    public BakerySearchResponseDTO(int resultCount, List<BakeryListResponseDTO> bakeryList) {
        this.resultCount = resultCount;
        this.bakeryList = bakeryList;
    }

    public static BakerySearchResponseDTO getEmptyBakerySearchResponseDTO() {
        return new BakerySearchResponseDTO(
                0,
                Collections.EMPTY_LIST
        );
    }
}
