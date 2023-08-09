package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@SuperBuilder
public class BakeryDetailResponseDTO extends BaseBakeryResponseDTO {
    private Long reviewCount;
    private BreadTypeResponseDTO breadType;
    private String homepage;
    private String address;
    private String openingTime;
    private String closedDay;
    private String phoneNumber;
    private List<MenuResponseDTO> menuList;
    private Boolean isBookMarked;
    private Long bookMarkCount;
}
