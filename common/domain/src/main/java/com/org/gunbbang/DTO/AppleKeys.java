package com.org.gunbbang.DTO;

import java.util.List;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AppleKeys {
  List<AppleKey> keys;

  public AppleKey getMatchingPublicKey(String extractedKeyId, String extractedAlg) {
    return keys.stream()
        .filter(key -> key.getKid().equals(extractedKeyId) && key.getAlg().equals(extractedAlg))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("identity token의 keyId & alg가 잘못되었습니다"));
  }
}
