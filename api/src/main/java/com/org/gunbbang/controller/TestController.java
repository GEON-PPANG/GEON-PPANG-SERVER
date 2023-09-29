package com.org.gunbbang.controller;

import com.org.gunbbang.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

  @GetMapping(value = "/security", name = "spring_security_test")
  public Long securityTest() {
    return SecurityUtil.getLoginMemberId();
  }

  @GetMapping(value = "/error", name = "error_test")
  public void errorTest() {
    throw new IllegalArgumentException("에러 로그 확인용 테스트");
  }
}
