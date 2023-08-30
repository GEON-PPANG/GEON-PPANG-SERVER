package com.org.gunbbang;

import com.org.gunbbang.DTO.RevokeAppleTokenRequestDTO;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "apple-feign", url = "https://appleid.apple.com")
public interface AppleFeignClient {
  @PostMapping(value = "/auth/revoke")
  void revokeAppleToken(@RequestBody @Valid RevokeAppleTokenRequestDTO request);
}
