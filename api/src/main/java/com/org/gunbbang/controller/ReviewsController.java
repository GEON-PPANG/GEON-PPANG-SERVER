package com.org.gunbbang.controller;

import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewCreateResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewDetailResponseDTO;
import com.org.gunbbang.support.errorType.SuccessType;
import com.org.gunbbang.service.AmplitudeService;
import com.org.gunbbang.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {
  private final ReviewService reviewService;
  private final AmplitudeService amplitudeService;

  @PostMapping(value = "/{bakeryId}", name = "리뷰_작성")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<ReviewCreateResponseDTO> createReview(
      @PathVariable("bakeryId") final Long bakeryId,
      @RequestBody @Valid final ReviewRequestDTO request) {

    Long memberId = SecurityUtil.getLoginMemberId();
    Long reviewId = reviewService.createReview(memberId, bakeryId, request);

    amplitudeService.sendUserProperty(memberId);
    return ApiResponse.success(
        SuccessType.CREATE_REVIEW_SUCCESS,
        ReviewCreateResponseDTO.builder().reviewId(reviewId).build());
  }

  @GetMapping(value = "/{reviewId}", name = "유저_리뷰_상세보기")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<ReviewDetailResponseDTO> getReviewedByMember(@PathVariable Long reviewId) {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.GET_REVIEW_DETAIL_MEMBER_SUCCESS,
        reviewService.getReviewedByMember(reviewId, memberId));
  }
}
