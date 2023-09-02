package com.org.gunbbang.DTO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import javax.validation.constraints.NotNull;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AppleRefreshTokenRequestDTO {
  @NotNull String clientId;
  @NotNull String clientSecret;
  @NotNull String code;
  String grantType = "authorization_code";
}
