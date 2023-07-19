package com.org.gunbbang.controller;

import com.org.gunbbang.BadRequestException;
import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewListResponseDTO;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import com.org.gunbbang.errorType.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewsController {
    private final ReviewService reviewService;

    @PostMapping(value = "/{bakeryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<Long> createReview(@PathVariable("bakeryId")final Long bakeryId, @RequestBody @Valid final ReviewRequestDTO request) {
        Long reviewId = reviewService.createReview(bakeryId, request);
        if (request.getIsLike()) {
            if(request.getKeywordList().isEmpty()){
                throw new BadRequestException(ErrorType.REQUEST_KEYWORDLIST_VALIDATION_EXCEPTION);
            }
            reviewService.createReviewRecommendKeyword(request.getKeywordList(), reviewId);
        }
        return ApiResponse.success(SuccessType.CREATE_REVIEW_SUCCESS, reviewId);
    }

    @GetMapping(value="/{reviewId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReviewDetailResponseDTO> getReviewedByMember(@PathVariable Long reviewId) {
        return ApiResponse.success(SuccessType.GET_REVIEW_DETAIL_MEMBER_SUCCESS, reviewService.getReviewedByMember(reviewId));
    }
}