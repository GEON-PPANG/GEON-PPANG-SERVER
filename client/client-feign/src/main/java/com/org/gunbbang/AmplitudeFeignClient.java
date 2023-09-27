package com.org.gunbbang;

import com.org.gunbbang.DTO.*;
import feign.Response;
import javax.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "amplitude-feign",
    url = "https://api2.amplitude.com",
    configuration = FeignConfig.class)
public interface AmplitudeFeignClient {
  @PostMapping(value = "/2/httpapi", consumes = MediaType.APPLICATION_JSON_VALUE)
  HttpV2ResponseDTO uploadRequest(@RequestBody @Valid HttpV2RequestDTO request);

  @PostMapping(value = "/identify", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
  Response identifyUserProperty(@RequestBody String request);
}
