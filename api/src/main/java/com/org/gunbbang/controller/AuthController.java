package com.org.gunbbang.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.org.gunbbang.AOP.annotation.SignupApiLog;
import com.org.gunbbang.PlatformType;
import com.org.gunbbang.Role;
import com.org.gunbbang.common.AuthType;
import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberWithdrawResponseDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.jwt.service.JwtService;
import com.org.gunbbang.login.AuthServiceProvider;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
  private final MemberService memberService;
  private final AuthServiceProvider authServiceProvider;
  private final JwtService jwtService;

  @PostMapping("/signup")
  @SignupApiLog
  // public ApiResponse<MemberSignUpResponseDTO> signUp(
  public ResponseEntity<MemberSignUpResponseDTO> signUp(
      @RequestHeader(value = "Social-Authorization", required = false)
          final String authorizationCode,
      @RequestBody final MemberSignUpRequestDTO request)
      throws JsonProcessingException {
    if (request.getPlatformType().equals(PlatformType.NONE)) {
      return ResponseEntity.ok().body(memberService.signUp(request));
    }
    Member customOAuth2User =
        authServiceProvider
            .getAuthService(request.getPlatformType())
            .loadMemberByToken(authorizationCode, request.getPlatformType());
    HttpHeaders httpHeaders = new HttpHeaders();
    getSocialLoginMemberToken(customOAuth2User, httpHeaders);
    return ResponseEntity.ok()
        .headers(httpHeaders)
        .body(
            MemberSignUpResponseDTO.builder()
                .memberId(customOAuth2User.getMemberId())
                .type(AuthType.LOGIN)
                .email(customOAuth2User.getEmail())
                .build());
  }

  private void getSocialLoginMemberToken(Member customOAuth2User, HttpHeaders httpHeaders) {
    String accessToken =
        jwtService.createAccessToken(customOAuth2User.getEmail(), customOAuth2User.getMemberId());
    httpHeaders.add("Authorization", accessToken);
    if (customOAuth2User.getRole().equals(Role.USER)) {
      String refreshToken = jwtService.createRefreshToken();
      jwtService.updateRefreshToken(customOAuth2User.getEmail(), refreshToken);
      httpHeaders.add("Authorization-refresh", refreshToken);
    }
  }

  public ApiResponse<MemberSignUpResponseDTO> signUpV1(
      @RequestBody final MemberSignUpRequestDTO request,
      @RequestHeader(name = "PlatformAccessToken", required = false) String platformAccessToken)
      throws Exception {
    return ApiResponse.success(
        SuccessType.SIGNUP_SUCCESS, memberService.signUp(request, platformAccessToken));
  }

  @DeleteMapping("/withdraw")
  public ApiResponse<MemberWithdrawResponseDTO> withdraw(
      @RequestHeader(name = "Apple-refresh", required = false) String appleRefreshToken)
      throws Exception {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.DELETE_MEMBER_SUCCESS, memberService.withdraw(memberId, appleRefreshToken));
  }
}
