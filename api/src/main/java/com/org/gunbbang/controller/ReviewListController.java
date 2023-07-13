package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.response.ReviewDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewListResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewListController {

    private final ReviewService reviewService;

    @GetMapping("/{bakeryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<ReviewListResponseDTO> getBakeryReviewList(@PathVariable("bakeryId") @Valid Long bakeryId) {
        ReviewListResponseDTO reviewListResponseDto= reviewService.getBakeryReviewList(bakeryId);
        return ApiResponse.success(SuccessType.GET_BAKERY_REVIEW_LIST_SUCCESS, reviewListResponseDto);
    }
}
