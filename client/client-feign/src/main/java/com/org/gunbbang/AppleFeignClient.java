package com.org.gunbbang;

import com.org.gunbbang.DTO.AppleKeys;
import com.org.gunbbang.DTO.AppleRefreshTokenRequestDTO;
import com.org.gunbbang.DTO.AppleRefreshTokenResponseDTO;
import com.org.gunbbang.DTO.RevokeAppleTokenRequestDTO;
import feign.Headers;
import feign.Response;
import java.awt.*;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "apple-feign", url = "https://appleid.apple.com")
public interface AppleFeignClient {
  @PostMapping(value = "/auth/revoke")
  @Headers("Content-Type: application/x-www-form-urlencoded")
  Response revokeAppleToken(@RequestBody @Valid RevokeAppleTokenRequestDTO request);

  @GetMapping(value = "/auth/keys")
  AppleKeys getAppleKeySet();

  @PostMapping("/auth/token")
  @Headers("Content-Type: application/x-www-form-urlencoded")
  AppleRefreshTokenResponseDTO getRefreshToken(
      @RequestBody @Valid AppleRefreshTokenRequestDTO request);
}
