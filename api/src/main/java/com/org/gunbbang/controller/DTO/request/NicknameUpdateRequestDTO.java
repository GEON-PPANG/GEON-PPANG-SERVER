package com.org.gunbbang.controller.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NicknameUpdateRequestDTO {
  @NotNull private String nickname;
}
