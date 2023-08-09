package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
public class BestBakeryListResponseDTO extends BaseBakeryResponseDTO {
    private Long reviewCount;
    private Boolean isBookMarked;
    private Long bookMarkCount;
}

