package com.org.gunbbang.auth.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.org.gunbbang.AppleFeignClient;
import com.org.gunbbang.slack.SlackSender;
import com.org.gunbbang.support.exception.AppleTokenRevokeException;
import com.org.gunbbang.support.exception.BadRequestException;
import com.org.gunbbang.DTO.AppleAuthCodeRequestDTO;
import com.org.gunbbang.DTO.AppleAuthResponseDTO;
import com.org.gunbbang.DTO.AppleKey;
import com.org.gunbbang.DTO.RevokeAppleTokenRequestDTO;
import com.org.gunbbang.support.exception.InvalidAppleAuthTokenException;
import com.org.gunbbang.support.errorType.ErrorType;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class AppleJwtService {
  @Value("${apple.api.key-id}")
  private String keyId;

  @Value("${apple.api.client-id}")
  private String clientId;

  @Value("${apple.api.team-id}")
  private String teamId;

  @Value("${apple.api.private-key-path}")
  private String privateKeyPath;

  private final SlackSender slackSender;

  private static final String URL = "https://appleid.apple.com";
  private static final String ALG = "ES256";
  private static final String EMAIL_CLAIM = "email";
  private static final String VALIDATION_GRANT_TYPE = "authorization_code";
  private static final String REVOKE_TOKEN_TYPE_HINT = "refresh_token";
  private final AppleFeignClient appleFeignClient;

  public String createAppleSecret() throws Exception {
    try {
      Map<String, Object> appleSecretHeader = new HashMap<>();
      appleSecretHeader.put("alg", ALG);
      appleSecretHeader.put("kid", keyId);

      long now = Instant.now().getEpochSecond();
      String appleSecret =
          JWT.create()
              .withHeader(appleSecretHeader)
              .withClaim("iss", teamId)
              .withClaim("iat", now)
              .withClaim("exp", now + 180000)
              .withClaim("aud", URL)
              .withClaim("sub", clientId)
              .sign(Algorithm.ECDSA256(getPrivateKey()));

      return appleSecret;
    } catch (Exception e) {
      log.error("%%%%%%%%%% apple secret 키를 발급하는데 에러가 발생했습니다 {} %%%%%%%%%%", e.getMessage());
      throw e;
    }
  }

  private ECPrivateKey getPrivateKey() throws IOException {
    try (FileReader fileReader = new FileReader(privateKeyPath)) {
      try (PemReader reader = new PemReader(fileReader)) {
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);
        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
      } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public String getEmailFromIdentityToken(String identityToken) {
    try {
      DecodedJWT decodedToken = JWT.decode(identityToken);
      AppleKey appleKey =
          appleFeignClient
              .getAppleKeySet()
              .getMatchingPublicKey(decodedToken.getKeyId(), decodedToken.getAlgorithm());

      return getEmailFromClaims(identityToken, appleKey);

    } catch (Exception e) {
      log.error(
          "%%%%%%%%%% 애플 key 가져오는 과정에서 에러 발생. 에러클래스: {} 에러메시지: {} %%%%%%%%%%",
          e.getClass().getName(), e.getMessage());
      throw e;
    }
  }

  private static String getEmailFromClaims(String identityToken, AppleKey appleKey) {
    PublicKey publicKey = getPublicKey(appleKey);

    try {
      DecodedJWT decodedIdentitytoken =
          JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey, null))
              .build()
              .verify(identityToken);
      return decodedIdentitytoken.getClaim(EMAIL_CLAIM).asString();
    } catch (Exception e) {
      log.error(
          "%%%%%%%%%% 애플 claim에서 email을 가져오는데 실패했습니다. 에러메시지: {} 에러클래스: {} %%%%%%%%%%",
          e.getMessage(), e.getClass().getName());
      throw e;
    }
  }

  private static PublicKey getPublicKey(AppleKey appleKey) {
    try {
      byte[] nBytes = Base64.getUrlDecoder().decode(appleKey.getN());
      byte[] eBytes = Base64.getUrlDecoder().decode(appleKey.getE());
      BigInteger n = new BigInteger(1, nBytes);
      BigInteger e = new BigInteger(1, eBytes);

      // RSAPublicKeySpec를 사용하여 공개 키 생성
      RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
      KeyFactory keyFactory = KeyFactory.getInstance(appleKey.getKty());
      return keyFactory.generatePublic(publicKeySpec);
    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
      log.error(
          "%%%%%%%%%% 애플 claim에서 email을 가져오는데 실패했습니다. 에러메시지: {} 에러클래스: {} %%%%%%%%%%",
          e.getMessage(), e.getClass().getName());
      throw new RuntimeException();
    }
  }

  // 애플 refreshToken revoke 처리
  public void revokeAppleTokens(String appleRefreshToken, Long memberId) throws Exception {
    if (appleRefreshToken == null) {
      slackSender.sendMessage(ErrorType.NO_APPLE_REFRESH_HEADER_EXCEPTION.getMessage() + memberId);
      throw new BadRequestException(
          ErrorType.NO_APPLE_REFRESH_HEADER_EXCEPTION,
          ErrorType.NO_APPLE_REFRESH_HEADER_EXCEPTION.getMessage() + memberId);
    }

    RevokeAppleTokenRequestDTO request =
        RevokeAppleTokenRequestDTO.of(
            clientId, createAppleSecret(), appleRefreshToken, REVOKE_TOKEN_TYPE_HINT);

    try {
      appleFeignClient.revokeAppleToken(request);
    } catch (Exception e) {
      log.error(" %%%%%%%%%% 애플 리프레시 토큰을 revoke하는 과정에서 에러 발생: {}", e.getMessage());
      throw new AppleTokenRevokeException(ErrorType.REVOKE_APPLE_REFRESH_TOKEN_FAIL_EXCEPTION);
    }
  }

  public AppleAuthResponseDTO validateAuthorizationCode(String authorizationCode) throws Exception {
    if (authorizationCode == null) {
      throw new BadRequestException(ErrorType.NO_REQUEST_HEADER_EXCEPTION);
    }

    AppleAuthCodeRequestDTO request =
        AppleAuthCodeRequestDTO.of(
            clientId, createAppleSecret(), authorizationCode, VALIDATION_GRANT_TYPE);

    try {
      return appleFeignClient.validateAuthorizationCode(request);
    } catch (Exception e) {
      log.error(
          "%%%%%%%%%% 애플 authorization code를 validate하는 과정에서 에러 발생: {} %%%%%%%%%%", e.getMessage());
      throw new InvalidAppleAuthTokenException(ErrorType.INVALID_APPLE_AUTH_TOKEN_EXCEPTION);
    }
  }
}
