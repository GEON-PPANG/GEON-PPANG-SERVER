package com.org.gunbbang.controller.DTO.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class NutrientTypeResponseDTO {
  @NotNull private Long nutrientTypeId;
}
