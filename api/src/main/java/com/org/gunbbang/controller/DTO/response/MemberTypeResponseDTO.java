package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberTypeResponseDTO {
  @NotNull private Long memberId;
  @NotNull private MainPurpose mainPurpose;
  @NotNull private BreadTypeResponseDTO breadType;
  @NotNull private NutrientTypeResponseDTO nutrientType;
}
