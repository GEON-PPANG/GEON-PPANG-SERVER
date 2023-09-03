package com.org.gunbbang.DTO;

import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

// 애플 토큰 revoke 요청 DTO
@Getter
@Builder
// @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class RevokeAppleTokenRequestDTO {
  @NotNull private String client_id;
  @NotNull private String client_secret;
  @NotNull private String token;
  @NotNull private String token_type_hint;

  public static RevokeAppleTokenRequestDTO of(
      String clientId, String appleSecret, String appleRefreshToken, String tokenTypeHint) {
    return RevokeAppleTokenRequestDTO.builder()
        .client_id(clientId)
        .client_secret(appleSecret)
        .token(appleRefreshToken)
        .token_type_hint(tokenTypeHint)
        .build();
  }
}
