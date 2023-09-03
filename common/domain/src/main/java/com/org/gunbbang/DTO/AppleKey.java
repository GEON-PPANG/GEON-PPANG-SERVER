package com.org.gunbbang.DTO;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AppleKey {
  String kty;
  String kid;
  String use;
  String alg;
  String n;
  String e;
}
