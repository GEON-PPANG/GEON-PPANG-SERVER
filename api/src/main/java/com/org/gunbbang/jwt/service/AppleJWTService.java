package com.org.gunbbang.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.security.KeyFactory;
import java.security.interfaces.ECPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class AppleJWTService {
  @Value("${apple.api.key-id}")
  private String keyId;

  @Value("${apple.api.client-id}")
  private String clientId;

  @Value("${apple.api.team-id}")
  private String teamId;

  @Value("${apple.api.private-key}")
  private String privateKey;

  private static final String url = "https://appleid.apple.com";
  private static final String alg = "ES256";

  public String createAppleSecret() throws Exception {
    try {
      Map<String, Object> appleSecretHeader = new HashMap<>();
      appleSecretHeader.put("alg", alg);
      appleSecretHeader.put("kid", keyId);

      long now = Instant.now().getEpochSecond();
      String appleSecret =
          JWT.create()
              .withHeader(appleSecretHeader)
              .withClaim("iss", teamId)
              .withClaim("iat", now)
              .withClaim("exp", now + 180)
              .withClaim("aud", url)
              .withClaim("sub", clientId)
              .sign(Algorithm.ECDSA256(stringToECPrivateKey(privateKey)));
      System.out.println("appleSecret 생성: " + appleSecret);
      return appleSecret;
    } catch (Exception e) {
      log.warn("apple secret 키를 발급하는데 에러가 발생했습니다 " + e.getMessage());
      throw e;
    }
  }

  private ECPrivateKey stringToECPrivateKey(String privateKey) throws Exception {
    try {
      byte[] decodedKey = Base64.getDecoder().decode(privateKey); // privateKey를 디코딩
      KeyFactory keyFactory = KeyFactory.getInstance("EC");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
      return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (Exception e) {
      log.warn("apple private 키를 ECPrivateKey로 변환하는데 에러가 발생했습니다 " + e.getMessage());
      throw e;
    }
  }
}
