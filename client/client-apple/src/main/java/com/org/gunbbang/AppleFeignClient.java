package com.org.gunbbang;

import com.org.gunbbang.DTO.AppleAuthCodeRequestDTO;
import com.org.gunbbang.DTO.AppleAuthResponseDTO;
import com.org.gunbbang.DTO.AppleKeys;
import com.org.gunbbang.DTO.RevokeAppleTokenRequestDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(
    name = "apple-feign",
    url = "https://appleid.apple.com",
    configuration = AppleFeignConfig.class)
public interface AppleFeignClient {
  @PostMapping(value = "/auth/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  void revokeAppleToken(@RequestBody @Valid RevokeAppleTokenRequestDTO request);

  @GetMapping(value = "/auth/keys")
  AppleKeys getAppleKeySet();

  @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  AppleAuthResponseDTO validateAuthorizationCode(
      @RequestBody @Valid AppleAuthCodeRequestDTO request);
}
