package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MemberTypeResponseDTO {
  @NotNull private Long memberId;
  @NotNull private MainPurpose mainPurpose;
  @NotNull private BreadTypeResponseDTO breadType;
  @NotNull private NutrientTypeResponseDTO nutrientType;
}
