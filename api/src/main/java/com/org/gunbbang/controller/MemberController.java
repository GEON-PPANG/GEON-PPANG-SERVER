package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDto;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import com.org.gunbbang.controller.DTO.MemberSignUpRequestDTO;
import com.org.gunbbang.util.Security.SecurityUtil;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponseDto> getMemberDetail() {
        MemberDetailResponseDto memberDetailResponseDto = memberService.getMemberDetail();
        return ApiResponse.success(SuccessType.GET_MYPAGE_SUCCESS, memberDetailResponseDto);
    }

    @PostMapping("/signup")
    public ApiResponse<MemberSignUpResponseDTO> signUp(@RequestBody MemberSignUpRequestDTO memberSignUpRequestDTO) throws Exception {
        return ApiResponse.success(SuccessType.SIGNUP_SUCCESS, memberService.signUp(memberSignUpRequestDTO));
    }

    @GetMapping("/security-test")
    public Long securityTest() {
        return SecurityUtil.getLoginMemberId();
    }

}
