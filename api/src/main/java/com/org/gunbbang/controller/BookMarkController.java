package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.BookMarkRequestDTO;
import com.org.gunbbang.controller.DTO.response.BookMarkResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.BookMarkService;
import com.org.gunbbang.service.MemberService;
import com.org.gunbbang.util.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BookMarkController {

    private final BookMarkService bookMarkService;

    @PostMapping("/bookMark/{bakeryId}")
    public ApiResponse<BookMarkResponseDTO> doBookMark(
            @RequestBody @Valid final BookMarkRequestDTO request,
            @PathVariable("bakeryId") Long bakeryId
    ) {
        Long memberId = SecurityUtil.getLoginMemberId();
        BookMarkResponseDTO response = bookMarkService.doBookMark(request.getIsAddingBookMark(), bakeryId, memberId);

        if (request.getIsAddingBookMark()) {
            return ApiResponse.success(SuccessType.CREATE_BOOKMARK_SUCCESS, response);
        }
        return ApiResponse.success(SuccessType.CANCEL_BOOKMARK_SUCCESS, response);
    }
}
