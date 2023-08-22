package com.org.gunbbang.service;

import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.request.ReviewReportRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewReportResponseDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.entity.ReviewReport;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.ReviewReportRepository;
import com.org.gunbbang.repository.ReviewRepository;
import com.org.gunbbang.util.mapper.ReviewReportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewReportService {
  private final ReviewReportRepository reviewReportRepository;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;

  public ReviewReportResponseDTO createReviewReport(
      ReviewReportRequestDTO request, Long memberId, Long reviewId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));
    Review foundReview =
        reviewRepository
            .findById(reviewId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_REVIEW_EXCEPTION));

    ReviewReport reviewReport =
        ReviewReportMapper.INSTANCE.toReviewReportEntity(request, foundMember, foundReview);
    System.out.println("review dto :" + request.toString());

    System.out.println("review report :" + reviewReport.toString());
    ReviewReport savedReviewReport = reviewReportRepository.saveAndFlush(reviewReport);

    return ReviewReportResponseDTO.builder()
        .reviewReportId(savedReviewReport.getReviewReportId())
        .build();
  }
}
