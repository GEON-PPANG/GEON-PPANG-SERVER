package com.org.gunbbang.controller.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ValidateNicknameRequestDTO {
  @NotNull private String nickname;

  @Override
  public String toString() {
    return "ValidateNicknameRequestDTO{" + "nickname='" + nickname + '\'' + '}';
  }
}
