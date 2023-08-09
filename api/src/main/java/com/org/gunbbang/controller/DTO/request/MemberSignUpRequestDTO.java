package com.org.gunbbang.controller.DTO.request;

import com.org.gunbbang.PlatformType;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberSignUpRequestDTO {
  @NotNull private String email;
  @NotNull private String password;
  @NotNull private String nickname;
  @NotNull private String mainPurpose;
  @NotNull private PlatformType platformType;
  @NotNull private BreadTypeRequestDTO breadType;
  @NotNull private NutrientTypeRequestDTO nutrientType;

  @Override
  public String toString() {
    return "MemberSignUpRequestDTO{"
        + "email='"
        + email
        + '\''
        + ", password='"
        + password
        + '\''
        + ", nickname='"
        + nickname
        + '\''
        + ", mainPurpose='"
        + mainPurpose
        + '\''
        + ", platformType="
        + platformType
        + ", breadType="
        + breadType
        + ", nutrientType="
        + nutrientType
        + '}';
  }
}
