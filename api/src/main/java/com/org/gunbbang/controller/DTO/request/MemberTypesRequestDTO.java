package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.MainPurpose;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class MemberTypesRequestDTO {
    @NotNull
    private MainPurpose mainPurpose;
    @NotNull
    private BreadTypeRequestDTO breadType;
    @NotNull
    private NutrientTypeRequestDTO nutrientType;

    @Override
    public String toString() {
        return "MemberTypesRequestDTO{" +
                "mainPurpose=" + mainPurpose +
                ", breadType=" + breadType +
                ", nutrientType=" + nutrientType +
                '}';
    }
}
