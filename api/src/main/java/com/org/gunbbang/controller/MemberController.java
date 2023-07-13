package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.BakeryListReviewedByMemberDTO;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberTypesResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.org.gunbbang.controller.DTO.request.MemberSignUpRequestDTO;
import com.org.gunbbang.util.Security.SecurityUtil;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final ReviewService reviewService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberDetailResponseDTO> getMemberDetail() {
        MemberDetailResponseDTO memberDetailResponseDto = memberService.getMemberDetail();
        return ApiResponse.success(SuccessType.GET_MYPAGE_SUCCESS, memberDetailResponseDto);
    }

    @PostMapping("/signup")
    public ApiResponse<MemberSignUpResponseDTO> signUp(@RequestBody final MemberSignUpRequestDTO memberSignUpRequestDTO) throws Exception {
        return ApiResponse.success(SuccessType.SIGNUP_SUCCESS, memberService.signUp(memberSignUpRequestDTO));
    }

    @GetMapping("/security-test")
    public Long securityTest() {
        return SecurityUtil.getLoginMemberId();
    }

    @PostMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberTypesResponseDTO> updateMemberTypes(@RequestBody final MemberTypesRequestDTO request) {
        Long memberId = SecurityUtil.getLoginMemberId();
        return ApiResponse.success(SuccessType.UPDATE_MEMBER_TYPES_SUCCESS, memberService.updateMemberTypes(request, memberId));
    }

    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<MemberTypesResponseDTO> getMemberTypes() {
        Long memberId = SecurityUtil.getLoginMemberId();
        return ApiResponse.success(SuccessType.GET_MEMBER_TYPES_SUCCESS, memberService.getMemberTypes(memberId));
    }

    @GetMapping("/reviews")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<BakeryListReviewedByMemberDTO>> getBakeryListReviewedByMember(){
        Long memberId = SecurityUtil.getLoginMemberId();
        return ApiResponse.success(SuccessType.GET_MEMBER_REVIEW_BAKERY_LIST_SUCCESS, reviewService.getBakeryListReviewedByMember(memberId));
    }
}
