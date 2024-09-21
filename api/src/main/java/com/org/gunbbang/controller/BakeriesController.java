package com.org.gunbbang.controller;

import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BakeryDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewListResponseDTO;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.support.errorType.SuccessType;
import com.org.gunbbang.service.BakeryListService;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bakeries")
public class BakeriesController {

  private final BakeryService bakeryService;
  private final ReviewService reviewService;
  private final BakeryListService bakeryListService;

  //    @GetMapping(value = "", name = "건빵집 리스트 조회")
  //    @ResponseStatus(HttpStatus.OK)
  //    public ResponseEntity<ApiResponse<Page<BakeryListResponseDTO>>> getBakeryList(
  //            @RequestParam("sortingOption") String sortingOption,
  //            @RequestParam("personalFilter") boolean personalFilter,
  //            @RequestParam("isHard") boolean isHard,
  //            @RequestParam("isDessert") boolean isDessert,
  //            @RequestParam("isBrunch") boolean isBrunch,
  //            @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
  //        if (SecurityUtil.checkAnonymousUser() && personalFilter) {
  //            return ResponseEntity.status(HttpStatus.FORBIDDEN)
  //                    .body(ApiResponse.error(ErrorType.ACCESS_DENIED));
  //        }
  //        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
  //        Page<BakeryListResponseDTO> bakeryListResponseDto =
  //                bakeryService.getBakeryList(
  //                        sortingOption, personalFilter, isHard, isDessert, isBrunch,
  // pageRequest);
  //        return ResponseEntity.ok(
  //                ApiResponse.success(SuccessType.GET_BAKERY_LIST_SUCCESS,
  // bakeryListResponseDto));
  //    }

  @GetMapping(value = "", name = "건빵집 리스트 조회")
  @ResponseStatus(HttpStatus.OK)
  public ResponseEntity<ApiResponse<Page<BakeryListResponseDTO>>> getBakeryListV2(
      @RequestParam("sortingOption") String sortingOption,
      @RequestParam("personalFilter") boolean personalFilter,
      @RequestParam("isHard") boolean isHard,
      @RequestParam("isDessert") boolean isDessert,
      @RequestParam("isBrunch") boolean isBrunch,
      @RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber) {
    if (SecurityUtil.checkAnonymousUser() && personalFilter) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN)
          .body(ApiResponse.error(ErrorType.ACCESS_DENIED));
    }
    PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
    Page<BakeryListResponseDTO> bakeryListResponseDto =
        bakeryListService.getBakeryList(
            sortingOption, personalFilter, isHard, isDessert, isBrunch, pageRequest);
    return ResponseEntity.ok(
        ApiResponse.success(SuccessType.GET_BAKERY_LIST_SUCCESS, bakeryListResponseDto));
  }

  @GetMapping(value = "/{bakeryId}", name = "건빵집_상세조회")
  @ResponseStatus(HttpStatus.OK)
  public ApiResponse<BakeryDetailResponseDTO> getBakeryDetail(
      @PathVariable("bakeryId") Long bakeryId) {
    BakeryDetailResponseDTO bakeryDetailResponseDTO = bakeryService.getBakeryDetail(bakeryId);
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
