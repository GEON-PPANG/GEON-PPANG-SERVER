package com.org.gunbbang.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.org.gunbbang.AppleFeignClient;
import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.DTO.AppleKey;
import com.org.gunbbang.DTO.AppleRefreshTokenRequestDTO;
import com.org.gunbbang.DTO.RevokeAppleTokenRequestDTO;
import com.org.gunbbang.errorType.ErrorType;
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

  private static final String url = "https://appleid.apple.com";
  private static final String alg = "ES256";
  private final AppleFeignClient appleFeignClient;

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
              .withClaim("exp", now + 180000)
              .withClaim("aud", url)
              .withClaim("sub", clientId)
              .sign(Algorithm.ECDSA256(getPrivateKey()));
      System.out.println("appleSecret 생성: " + appleSecret);
      System.out.println("getPrivateKey 생성: " + getPrivateKey().toString());

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

  private ECPrivateKey getPrivateKey() throws IOException {
    FileReader fileReader = new FileReader(privateKeyPath);
    try {
      PemReader reader = new PemReader(fileReader);
      try {
        PemObject pemObject = reader.readPemObject();
        byte[] content = pemObject.getContent();
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(content);
        return (ECPrivateKey) keyFactory.generatePrivate(keySpec);
      } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
        throw new RuntimeException(e);
      } finally {
        reader.close();
      }
    } finally {
      fileReader.close();
    }
  }

  // 클라이언트에서 받아온 identityToken 복호화
  // 애플은 토큰 만료 시 에러가 바로 안터지고 그냥 decodedToken이 null이 반환됨
  // 복호화한 토큰에서 keyId 가져옴
  public String getEmailFromIdentityToken(String identityToken)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    try {
      DecodedJWT decodedToken = JWT.decode(identityToken);
      String extractedKeyId = decodedToken.getKeyId();
      String extractedAlg = decodedToken.getAlgorithm();
      System.out.println("추출한 KeyId: " + extractedKeyId);
      System.out.println("추출한 alg: " + alg);

      AppleKey appleKey =
          appleFeignClient.getAppleKeySet().getMatchingPublicKey(extractedKeyId, extractedAlg);
      System.out.println("applePublicKey: " + appleKey.toString());

      return getEmailFromClaims(identityToken, appleKey);

    } catch (Exception e) {
      log.warn("애플 key 가져오는 과정에서 에러 발생: " + e.getClass().getName());
      log.warn("애플 key 가져오는 과정에서 에러 발생: " + e.getMessage());
      log.warn(e.getStackTrace()[0].toString());
      log.warn(e.getStackTrace()[1].toString());
      log.warn(e.getStackTrace()[2].toString());

      throw e;
    }
  }

  private static String getEmailFromClaims(String identityToken, AppleKey appleKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    PublicKey publicKey = getPublicKey(appleKey);
    try {
      DecodedJWT decodedIdentitytoken =
          JWT.require(Algorithm.RSA256((RSAPublicKey) publicKey, null))
              .build()
              .verify(identityToken);

      String email = decodedIdentitytoken.getClaim("email").toString();
      System.out.println("payload: " + email);
      return email;
    } catch (SignatureVerificationException e) {
      log.warn("signature of identity token is not valid");
      throw e;
    } catch (TokenExpiredException e) {
      log.warn("identity token is expired");
      throw e;
    } catch (JWTDecodeException e) {
      log.warn("decoding jwt failed");
      throw e;
    } catch (JWTVerificationException e) {
      log.warn("JWTVerificationException");
      throw e;
    }
  }

  private static PublicKey getPublicKey(AppleKey appleKey)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] nBytes = Base64.getUrlDecoder().decode(appleKey.getN());
    byte[] eBytes = Base64.getUrlDecoder().decode(appleKey.getE());
    BigInteger n = new BigInteger(1, nBytes);
    BigInteger e = new BigInteger(1, eBytes);

    // RSAPublicKeySpec를 사용하여 공개 키 생성
    RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
    KeyFactory keyFactory = KeyFactory.getInstance(appleKey.getKty());
    PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
    System.out.println("publicKey: " + publicKey.toString());
    return publicKey;
  }

  // 애플 refreshToken revoke처리
  public void revokeAppleTokens(String appleRefreshToken) throws Exception {
    if (appleRefreshToken == null) {
      throw new BadRequestException(ErrorType.NO_REQUEST_HEADER_EXCEPTION);
    }

    String appleSecret = createAppleSecret();
    RevokeAppleTokenRequestDTO refreshRevokeRequest =
        RevokeAppleTokenRequestDTO.builder()
            .client_id(clientId)
            .client_secret(appleSecret)
            .token(appleRefreshToken)
            .token_type_hint("refresh_token")
            .build();

    try {
      int statusCode = appleFeignClient.revokeAppleToken(refreshRevokeRequest).status();
      if (statusCode == 400) {
        throw new IllegalAccessException("애플 토큰 revoke에 실패했습니다.");
      }
    } catch (Exception e) {
      log.warn("애플 토큰 revoke하는 과정에서 에러 발생: " + e.getMessage());
      throw e;
    }
  }

  public String getAppleRefreshToken(String AuthorizationCode) throws Exception {
    AppleRefreshTokenRequestDTO request =
        AppleRefreshTokenRequestDTO.builder()
            .clientSecret(createAppleSecret())
            .code(AuthorizationCode)
            .build();
    return appleFeignClient.getRefreshToken(request).getRefreshToken();
  }
}
