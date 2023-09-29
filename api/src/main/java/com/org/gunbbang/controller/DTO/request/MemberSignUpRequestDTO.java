package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.PlatformType;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class MemberSignUpRequestDTO {
  private String email;
  private String password;
  private String nickname;
  @NotNull private PlatformType platformType;
}
