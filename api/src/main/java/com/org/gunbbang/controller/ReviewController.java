package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewRequestDto;
import com.org.gunbbang.controller.DTO.response.ReviewIdResponseDto;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import com.org.gunbbang.errorType.SuccessType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    ReviewService reviewService;

    @PostMapping(value = "/{bakeryId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewIdResponseDto> createReview(@PathVariable("bakery-id") final Long bakeryId, @Valid final ReviewRequestDto request) {
        Long reviewId = reviewService.createReview(bakeryId, request);
        return ApiResponse.success(SuccessType.CREATE_REVIEW_SUCCESS, ReviewIdResponseDto.builder().reviewId(reviewId).build());
    }
}
