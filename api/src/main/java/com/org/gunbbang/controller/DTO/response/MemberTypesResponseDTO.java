package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTypesResponseDTO {
    @NotNull
    private Long memberId;
    @NotNull
    private MainPurpose mainPurpose;
    @NotNull
    private BreadTypeResponseDto breadType;
    @NotNull
    private NutrientTypeResponseDTO nutrientType;
}