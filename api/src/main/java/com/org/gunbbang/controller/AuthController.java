package com.org.gunbbang.controller;

import com.org.gunbbang.AOP.annotation.SignupApiLog;
import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final MemberService memberService;

  @PostMapping("/signup")
  @SignupApiLog
  public ApiResponse<MemberSignUpResponseDTO> signUp(
      @RequestBody final MemberSignUpRequestDTO request) {
    return ApiResponse.success(SuccessType.SIGNUP_SUCCESS, memberService.signUp(request));
  }
}
