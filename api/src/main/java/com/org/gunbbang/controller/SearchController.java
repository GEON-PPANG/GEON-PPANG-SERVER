package com.org.gunbbang.controller;

import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BakerySearchResponseDTO;
import com.org.gunbbang.support.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
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

  @GetMapping(value = "/bakeries", name = "건빵집_검색")
  public ApiResponse<BakerySearchResponseDTO> searchBakery(@RequestParam final String searchTerm) {
    return ApiResponse.success(
        SuccessType.SEARCH_BAKERIES_SUCCESS, bakeryService.getBakeriesBySearch(searchTerm.trim()));
  }
}
