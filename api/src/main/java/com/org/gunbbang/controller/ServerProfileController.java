package com.org.gunbbang.controller;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class ServerProfileController {
  private final Environment env;

  @GetMapping(value = "/profile", name = "서버_env_조회")
  public String getProfile() {
    return Arrays.stream(env.getActiveProfiles()).findFirst().orElse("");
  }
}
