package com.org.gunbbang.controller;

import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.request.NicknameUpdateRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.service.ReviewService;
import com.org.gunbbang.util.security.*;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

  private final MemberService memberService;
  private final ReviewService reviewService;
  private final BakeryService bakeryService;

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<MemberDetailResponseDTO> getMemberDetail() {
    MemberDetailResponseDTO memberDetailResponseDto = memberService.getMemberDetail();
    return ApiResponse.success(SuccessType.GET_MYPAGE_SUCCESS, memberDetailResponseDto);
  }

  @PostMapping("/types")
  public ApiResponse<MemberTypeResponseDTO> updateMemberTypes(
      @Valid @RequestBody final MemberTypesRequestDTO request) {
    Long memberId = SecurityUtil.getLoginMemberId();
    String nickname = SecurityUtil.getLoginMemberNickname();
    return ApiResponse.success(
        SuccessType.UPDATE_MEMBER_TYPES_SUCCESS,
        memberService.updateMemberTypes(request, memberId, nickname));
  }

  @GetMapping("/types")
  public ApiResponse<MemberTypeResponseDTO> getMemberTypes() {
    Map<String, Object> loginMemberInfo = SecurityUtil.getLoginMemberInfo();
    return ApiResponse.success(
        SuccessType.GET_MEMBER_TYPES_SUCCESS, memberService.getMemberTypes(loginMemberInfo));
  }

  @GetMapping("/reviews")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<List<BakeryListReviewedByMemberDTO>> getBakeryListReviewedByMember() {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.GET_MEMBER_REVIEW_BAKERY_LIST_SUCCESS,
        reviewService.getBakeryListReviewedByMember(memberId));
  }

  @GetMapping("/bookMarks")
  public ApiResponse<List<BakeryListResponseDTO>> getBookMarkedBakeries() {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.GET_BOOKMARKED_BAKERIES_SUCCESS, bakeryService.getBookMarkedBakeries(memberId));
  }

  @GetMapping("/nickname")
  public ApiResponse<MemberNicknameResponseDTO> getLoginMemberNickname() {
    String nickname = SecurityUtil.getLoginMemberNickname();
    return ApiResponse.success(
        SuccessType.GET_MEMBER_NICKNAME_SUCCESS,
        MemberNicknameResponseDTO.builder().nickname(nickname).build());
  }

  // TODO: 원래 게스트였던 회원의 경우 refresh 토큰 발급 & 업데이트 & 반환해야 함
  @PostMapping("/nickname")
  public ApiResponse<NicknameUpdateResponseDTO> updateLoginMemberNickname(
      @RequestBody @Valid final NicknameUpdateRequestDTO request) {
    Long memberId = SecurityUtil.getLoginMemberId();
    NicknameUpdateResponseDTO response =
        memberService.updateMemberNickname(memberId, request.getNickname());
    return ApiResponse.success(SuccessType.UPDATE_MEMBER_NICKNAME_SUCCESS, response);
  }
}
