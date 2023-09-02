package com.org.gunbbang.controller;

import com.org.gunbbang.AOP.annotation.SignupApiLog;
import com.org.gunbbang.Role;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberWithdrawResponseDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.jwt.util.JwtService;
import com.org.gunbbang.service.AuthServiceProvider;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import com.org.gunbbang.util.mapper.MemberMapper;
import com.org.gunbbang.util.security.SecurityUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
  private final MemberService memberService;
  private final AuthServiceProvider authServiceProvider;
  private final JwtService jwtService;

  @PostMapping("/signup")
  @SignupApiLog
  public ApiResponse<MemberSignUpResponseDTO> signUpV2(
      @RequestHeader(value = "Platform-token", required = false) final String platformToken,
      @RequestBody final MemberSignUpRequestDTO request,
      HttpServletResponse response)
      throws Exception {

    SignedUpMemberVO vo =
        authServiceProvider
            .getAuthService(request.getPlatformType())
            .saveMemberOrLogin(platformToken, request);
    log.info("####### vo 받아온 내용  #######" + vo.toString());

    jwtService.setSignedUpMemberToken(vo, response);
    return ApiResponse.success(
        SuccessType.SIGNUP_SUCCESS, MemberMapper.INSTANCE.toMemberSignUpResponseDTO(vo));
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

  public ApiResponse<MemberSignUpResponseDTO> signUp(
      @RequestBody final MemberSignUpRequestDTO request,
      @RequestHeader(name = "PlatformAccessToken", required = false) String platformAccessToken)
      throws Exception {
    return ApiResponse.success(SuccessType.SIGNUP_SUCCESS, memberService.signUp(request));
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
