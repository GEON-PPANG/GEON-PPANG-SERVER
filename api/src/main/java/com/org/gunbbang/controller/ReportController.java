package com.org.gunbbang.controller;

import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.common.DTO.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewReportRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewReportResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.ReviewReportService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
  private final ReviewReportService reviewReportService;

  @PostMapping(value = "/review/{reviewId}", name = "리뷰_신고")
  public ApiResponse<ReviewReportResponseDTO> createReviewReport(
      @RequestBody @Valid final ReviewReportRequestDTO request,
      @PathVariable("reviewId") final Long reviewId) {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.CREATE_REVIEW_REPORT_SUCCESS,
        reviewReportService.createReviewReport(request, memberId, reviewId));
  }
}
