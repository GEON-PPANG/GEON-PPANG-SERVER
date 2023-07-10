package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDto;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import antlr.StringUtils;
import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.controller.DTO.MemberSignUpRequestDTO;
import com.org.gunbbang.controller.DTO.MemberSignUpResponseDTO;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponseDto> getMemberDetail(@RequestHeader("memberId") @Valid Long memberId) {
        MemberDetailResponseDto memberDetailResponseDto = memberService.getMemberDetail(memberId);
        return ApiResponse.success(SuccessType.GET_MYPAGE_SUCCESS, memberDetailResponseDto);
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody MemberSignUpRequestDTO memberSignUpRequestDTO) throws Exception {
        System.out.println("회원가입");
        memberService.signUp(memberSignUpRequestDTO);
        return "회원가입 성공";
    }

    @GetMapping("/security-test")
    public Long securityTest() {
        return SecurityUtil.getLoginMemberId();
    }

}
