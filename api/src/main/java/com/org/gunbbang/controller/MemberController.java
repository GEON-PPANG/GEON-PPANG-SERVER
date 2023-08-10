package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.MemberTypesRequestDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.service.ReviewService;
import com.org.gunbbang.util.Security.SecurityUtil;
import java.util.List;
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
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<MemberTypesResponseDTO> updateMemberTypes(
      @Valid @RequestBody final MemberTypesRequestDTO request) {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.UPDATE_MEMBER_TYPES_SUCCESS,
        memberService.updateMemberTypes(request, memberId));
  }

  @GetMapping("/types")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<MemberTypesResponseDTO> getMemberTypes() {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.GET_MEMBER_TYPES_SUCCESS, memberService.getMemberTypes(memberId));
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
        SuccessType.GET_BOOKMARKED_BAKERIES, bakeryService.getBookMarkedBakeries(memberId));
  }
}
