package com.org.gunbbang.controller;

import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {

  @GetMapping("/security")
  public Long securityTest() {
    return SecurityUtil.getLoginMemberId();
  }

  @GetMapping("/error")
  public void errorTest() {
    throw new IllegalArgumentException("에러 로그 확인용 테스트");
  }
}
