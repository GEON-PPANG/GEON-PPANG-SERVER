package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDto;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import com.org.gunbbang.errorType.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(value = "/{bakeryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse createReview(@PathVariable("bakeryId") @Valid final Long bakeryId, @RequestBody @Valid final ReviewRequestDto request) {
        System.out.println(request.getIsLike());
        Long reviewId = reviewService.createReview(bakeryId,request);
        if(request.getIsLike()) {
            reviewService.createReviewRecommendKeyword(request.getKeywordList(), reviewId);
        }
        return ApiResponse.success(SuccessType.CREATE_REVIEW_SUCCESS);
    }
}
