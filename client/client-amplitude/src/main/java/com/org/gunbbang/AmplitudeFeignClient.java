package com.org.gunbbang;

import com.org.gunbbang.DTO.*;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient(
    name = "amplitude-feign",
    url = "https://api2.amplitude.com",
    configuration = AmplitudeFeignConfig.class)
public interface AmplitudeFeignClient {
  @PostMapping(value = "/2/httpapi", consumes = MediaType.APPLICATION_JSON_VALUE)
  HttpV2ResponseDTO uploadRequest(@RequestBody @Valid HttpV2RequestDTO request);

  // TODO!!: 안될 것 같은데
  @PostMapping(value = "/identify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  void identifyUserProperty(@RequestBody String request);
}
