package com.org.gunbbang.controller.DTO.response;

import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class BreadTypeResponseDTO {
  @NotNull private Long breadTypeId;
  @NotNull private String breadTypeTag;
}
