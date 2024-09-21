package com.org.gunbbang.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppleAuthResponseDTO {
  String access_token;
  String token_type;
  String expires_in;
  String refresh_token;
  String id_token;
}
