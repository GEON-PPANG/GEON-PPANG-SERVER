package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BestBakeryListResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BestContentController {
    private final BakeryService bakeryService;
    private final ReviewService reviewService;

    @GetMapping("/bakeries/best")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<BestBakeryListResponseDTO>> getBestBakeries () {
        return ApiResponse.success(SuccessType.GET_BEST_BAKERIES_SUCCESS, bakeryService.getBestBakeries());
    }
}
