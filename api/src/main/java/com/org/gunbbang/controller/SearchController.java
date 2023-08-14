package com.org.gunbbang.controller;

import com.org.gunbbang.AOP.annotation.SearchApiLog;
import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BakerySearchResponseDTO;
import com.org.gunbbang.controller.DTO.response.BakerySearchResponseDTOV2;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
public class SearchController {

  private final BakeryService bakeryService;

  @GetMapping("/bakeries")
  @SearchApiLog
  public ApiResponse<BakerySearchResponseDTO> searchBakery(@RequestParam final String searchTerm) {
    return ApiResponse.success(
        SuccessType.SEARCH_BAKERIES_SUCCESS, bakeryService.getBakeriesBySearch(searchTerm.trim()));
  }

  @GetMapping("/v2/bakeries")
  @SearchApiLog
  public ApiResponse<BakerySearchResponseDTOV2> searchBakeryV2(
      @RequestParam final String bakeryName) {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.SEARCH_BAKERIES_SUCCESS,
        bakeryService.getBakeriesByNameV2(bakeryName.strip(), memberId));
  }
}
