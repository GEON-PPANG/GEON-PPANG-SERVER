package com.org.gunbbang.controller;

import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberWithdrawResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.jwt.service.JwtService;
import com.org.gunbbang.service.AmplitudeService;
import com.org.gunbbang.service.AuthServiceProvider;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import com.org.gunbbang.util.mapper.MemberMapper;
import com.org.gunbbang.util.security.SecurityUtil;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
  private final MemberService memberService;
  private final AuthServiceProvider authServiceProvider;
  private final JwtService jwtService;
  private final AmplitudeService amplitudeService;

  @PostMapping(value = "/signup", name = "회원가입")
  public ApiResponse<MemberSignUpResponseDTO> signUp(
      @RequestHeader(value = "Platform-token", required = false) final String platformToken,
      @RequestBody @Valid final MemberSignUpRequestDTO request,
      HttpServletResponse response)
      throws Exception {

    SignedUpMemberVO vo =
        authServiceProvider
            .getAuthService(request.getPlatformType())
            .saveMemberOrLogin(platformToken, request);

    jwtService.setSignedUpMemberToken(vo, response);
    amplitudeService.sendUserProperty(vo.getMemberId(), null);
    return ApiResponse.success(
        SuccessType.SIGNUP_SUCCESS, MemberMapper.INSTANCE.toMemberSignUpResponseDTO(vo));
  }

  @DeleteMapping(value = "/withdraw", name = "회원탈퇴")
  public ApiResponse<MemberWithdrawResponseDTO> withdraw(
      @RequestHeader(name = "Apple-refresh", required = false) String appleRefreshToken)
      throws Exception {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.DELETE_MEMBER_SUCCESS, memberService.withdraw(memberId, appleRefreshToken));
  }
}
