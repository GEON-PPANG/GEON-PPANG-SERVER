package com.org.gunbbang.controller;

import com.org.gunbbang.common.dto.ApiResponse;
import com.org.gunbbang.controller.DTO.request.ReviewReportRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewReportResponseDTO;
import com.org.gunbbang.errorType.SuccessType;
import com.org.gunbbang.service.ReviewReportService;
import com.org.gunbbang.util.security.SecurityUtil;
import java.io.IOException;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {
  private final ReviewReportService reviewReportService;

  @PostMapping("/review/{reviewId}")
  public ApiResponse<ReviewReportResponseDTO> createReviewReport(
      @RequestBody @Valid final ReviewReportRequestDTO request,
      @PathVariable("reviewId") final Long reviewId)
      throws IOException {
    Long memberId = SecurityUtil.getLoginMemberId();
    return ApiResponse.success(
        SuccessType.CREATE_REVIEW_REPORT_SUCCESS,
        reviewReportService.createReviewReport(request, memberId, reviewId));
  }
}