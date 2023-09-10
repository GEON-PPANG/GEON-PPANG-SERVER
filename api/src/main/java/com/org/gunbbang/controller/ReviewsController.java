package com.org.gunbbang.controller;

import com.org.gunbbang.AOP.annotation.BakeryIdApiLog;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewCreateResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewDetailResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.ReviewService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {
  private final ReviewService reviewService;

  @PostMapping(value = "/{bakeryId}")
  @ResponseStatus(HttpStatus.CREATED)
  @BakeryIdApiLog
  public ApiResponse<ReviewCreateResponseDTO> createReview(
      @PathVariable("bakeryId") final Long bakeryId,
      @RequestBody @Valid final ReviewRequestDTO request) {
    Long reviewId = reviewService.createReview(bakeryId, request);
    return ApiResponse.success(
        SuccessType.CREATE_REVIEW_SUCCESS,
        ReviewCreateResponseDTO.builder().reviewId(reviewId).build());
  }

  @GetMapping(value = "/{reviewId}")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<ReviewDetailResponseDTO> getReviewedByMember(@PathVariable Long reviewId) {
    return ApiResponse.success(
        SuccessType.GET_REVIEW_DETAIL_MEMBER_SUCCESS, reviewService.getReviewedByMember(reviewId));
  }
}
