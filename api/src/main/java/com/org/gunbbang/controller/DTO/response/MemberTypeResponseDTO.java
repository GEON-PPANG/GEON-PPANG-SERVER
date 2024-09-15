package com.org.gunbbang.controller.DTO.response;

import com.org.gunbbang.MainPurpose;
import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class MemberTypeResponseDTO {
  @NotNull private Long memberId;
  @NotNull private MainPurpose mainPurpose;
  @NotNull private String nickname;
  @NotNull private List<BreadTypeResponseDTO> breadTypeList;
  @NotNull private List<NutrientTypeResponseDTO> nutrientTypeList;
}
