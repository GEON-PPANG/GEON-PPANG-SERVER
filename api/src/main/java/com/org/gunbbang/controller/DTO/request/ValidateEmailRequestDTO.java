package com.org.gunbbang.controller.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ValidateEmailRequestDTO {
  @NotNull private String email;

  @Override
  public String toString() {
    return "ValidateEmailRequestDTO{" + "email='" + email + '\'' + '}';
  }
}
