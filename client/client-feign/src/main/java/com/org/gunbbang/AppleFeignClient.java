package com.org.gunbbang;

import com.org.gunbbang.DTO.AppleAuthCodeRequestDTO;
import com.org.gunbbang.DTO.AppleAuthResponseDTO;
import com.org.gunbbang.DTO.AppleKeys;
import com.org.gunbbang.DTO.RevokeAppleTokenRequestDTO;
import feign.Response;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "apple-feign",
    url = "https://appleid.apple.com",
    configuration = FeignConfig.class)
public interface AppleFeignClient {
  @PostMapping(value = "/auth/revoke", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  Response revokeAppleToken(@RequestBody @Valid RevokeAppleTokenRequestDTO request);

  @GetMapping(value = "/auth/keys")
  AppleKeys getAppleKeySet();

  @PostMapping(value = "/auth/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  AppleAuthResponseDTO validateAuthorizationCode(
      @RequestBody @Valid AppleAuthCodeRequestDTO request);
}
