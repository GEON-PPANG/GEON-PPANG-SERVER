package com.org.gunbbang.controller.DTO.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuperBuilder
public class BakerySearchResponseDTO {
    private int resultCount;
    private List<BakeryListResponseDTO> bakeryList;
}
