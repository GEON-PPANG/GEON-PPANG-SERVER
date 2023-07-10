package com.org.gunbbang.controller.DTO.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class BreadTypeRequestDTO {
    @NotNull
    private boolean isGlutenFree;
    @NotNull
    private boolean isVegan;
    @NotNull
    private boolean isNutFree;
    @NotNull
    private boolean isSugarFree;
}
