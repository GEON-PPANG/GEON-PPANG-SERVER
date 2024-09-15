package com.org.gunbbang.controller.DTO.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class BreadTypeResponseDTO {
  @NotNull private Long breadTypeId;
}
