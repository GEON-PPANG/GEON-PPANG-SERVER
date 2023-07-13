package com.org.gunbbang.controller.DTO.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuResponseDTO {
    private Long menuId;
    private String menuName;
    private int menuPrice;

    @Builder
    public MenuResponseDTO(Long menuId, String menuName, int menuPrice) {
        this.menuId = menuId;
        this.menuName = menuName;
        this.menuPrice = menuPrice;
    }
}
