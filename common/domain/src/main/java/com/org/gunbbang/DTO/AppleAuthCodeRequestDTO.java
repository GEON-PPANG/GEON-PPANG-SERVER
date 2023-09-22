package com.org.gunbbang.DTO;

import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class AppleAuthCodeRequestDTO {
  @NotNull String client_id;
  @NotNull String client_secret;
  @NotNull String code;
  @NotNull String grant_type;

  public static AppleAuthCodeRequestDTO of(
      String clientId, String appleSecret, String authorizationCode, String grantType) {
    return AppleAuthCodeRequestDTO.builder()
        .client_id(clientId)
        .client_secret(appleSecret)
        .code(authorizationCode)
        .grant_type(grantType)
        .build();
  }
}
