package com.org.gunbbang.controller;

import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BakeryDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewListResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.service.ReviewService;
import com.org.gunbbang.util.security.SecurityUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bakeries")
public class BakeriesController {

  private final BakeryService bakeryService;
  private final ReviewService reviewService;

  @GetMapping(value = "", name = "건빵집 리스트 조회")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<List<BakeryListResponseDTO>> getBakeryList(
      @RequestParam("sortingOption") String sortingOption,
      @RequestParam("personalFilter") boolean personalFilter,
      @RequestParam("isHard") boolean isHard,
      @RequestParam("isDessert") boolean isDessert,
      @RequestParam("isBrunch") boolean isBrunch) {
    List<BakeryListResponseDTO> bakeryListResponseDto =
        bakeryService.getBakeryList(sortingOption, personalFilter, isHard, isDessert, isBrunch);
    return ApiResponse.success(SuccessType.GET_BAKERY_LIST_SUCCESS, bakeryListResponseDto);
  }

  @GetMapping(value = "/{bakeryId}", name = "건빵집_상세조회")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<BakeryDetailResponseDTO> getBakeryDetail(
      @PathVariable("bakeryId") Long bakeryId) {
    Long memberId = SecurityUtil.getLoginMemberId();
    BakeryDetailResponseDTO bakeryDetailResponseDTO =
        bakeryService.getBakeryDetail(memberId, bakeryId);
    return ApiResponse.success(SuccessType.GET_BAKERY_DETAIL_SUCCESS, bakeryDetailResponseDTO);
  }

  @GetMapping(value = "/{bakeryId}/reviews", name = "건빵집_리뷰_조회")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<ReviewListResponseDTO> getBakeryReviewList(
      @PathVariable("bakeryId") Long bakeryId) {
    ReviewListResponseDTO reviewListResponseDto = reviewService.getBakeryReviewList(bakeryId);
    return ApiResponse.success(SuccessType.GET_BAKERY_REVIEW_LIST_SUCCESS, reviewListResponseDto);
  }
}
