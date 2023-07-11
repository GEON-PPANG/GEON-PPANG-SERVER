package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BakeryDetailResponseDto;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDto;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bakery")
public class BakeryController {
    private final BakeryService bakeryService;

    @GetMapping("/detail/{bakeryId}")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<BakeryDetailResponseDto> getMemberDetail(@PathVariable("bakeryId") @Valid Long bakeryId){
        Long memberId = SecurityUtil.getLoginMemberId();
        BakeryDetailResponseDto bakeryDetailResponseDto = bakeryService.getBakeryDetail(memberId, bakeryId);
        return ApiResponse.success(SuccessType.GET_BAKERY_DETAIL_SUCCESS, bakeryDetailResponseDto);
    }

}
