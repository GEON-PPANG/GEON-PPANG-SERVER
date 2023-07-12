package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDto;
import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryListResponseDTO;
import com.org.gunbbang.controller.DTO.response.BestBakeryListResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BakeryService;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bakeries")
public class BakeriesController {

    private final BakeryService bakeryService;
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<BakeryListResponseDto>> getMemberDetail(@RequestParam("sort") @Valid String sort,
                                                                    @RequestParam("isHard") @Valid Boolean isHard,
                                                                    @RequestParam("isDessert") @Valid Boolean isDessert,
                                                                    @RequestParam("isBrunch") @Valid Boolean isBrunch){
        Long memberId = SecurityUtil.getLoginMemberId();
        List<BakeryListResponseDto> bakeryListResponseDto = bakeryService.getBakeryList(memberId, sort,isHard,isDessert,isBrunch);
        return ApiResponse.success(SuccessType.GET_MYPAGE_SUCCESS, bakeryListResponseDto);
    }

    @GetMapping("/best")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<List<BestBakeryListResponseDTO>> getBestBakeries () {
        return ApiResponse.success(SuccessType.GET_BEST_BAKERIES_SUCCESS, bakeryService.getBestBakeries());
    }
}
