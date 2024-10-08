package com.org.gunbbang.controller;

import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.BookMarkRequestDTO;
import com.org.gunbbang.controller.DTO.response.BookMarkResponseDTO;
import com.org.gunbbang.support.errorType.SuccessType;
import com.org.gunbbang.service.AmplitudeService;
import com.org.gunbbang.service.BookMarkService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bookMarks")
public class BookMarkController {

  private final BookMarkService bookMarkService;
  private final AmplitudeService amplitudeService;

  @PostMapping(value = "/{bakeryId}", name = "북마크")
  public ApiResponse<BookMarkResponseDTO> doBookMark(
      @PathVariable final Long bakeryId, @RequestBody @Valid final BookMarkRequestDTO request) {
    Long memberId = SecurityUtil.getLoginMemberId();
    BookMarkResponseDTO response =
        bookMarkService.doBookMark(request.getIsAddingBookMark(), bakeryId, memberId);

    amplitudeService.sendUserProperty(memberId);
    if (response.getIsBookMarked()) {
      return ApiResponse.success(SuccessType.CREATE_BOOKMARK_SUCCESS, response);
    }
    return ApiResponse.success(SuccessType.CANCEL_BOOKMARK_SUCCESS, response);
  }
}
