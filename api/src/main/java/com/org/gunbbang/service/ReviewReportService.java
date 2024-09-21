package com.org.gunbbang.service;

import com.org.gunbbang.slack.SlackSender;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.controller.DTO.request.ReviewReportRequestDTO;
import com.org.gunbbang.controller.DTO.response.ReviewReportResponseDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.entity.ReviewReport;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.MemberRepository;
import com.org.gunbbang.repository.ReviewReportRepository;
import com.org.gunbbang.repository.ReviewRepository;
import com.org.gunbbang.VO.ReviewReportSlackVO;
import com.org.gunbbang.util.mapper.ReviewReportMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewReportService {
  private final ReviewReportRepository reviewReportRepository;
  private final MemberRepository memberRepository;
  private final ReviewRepository reviewRepository;
  private final SlackSender slackSender;

  public ReviewReportResponseDTO createReviewReport(
      ReviewReportRequestDTO request, Long memberId, Long reviewId) {
    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_USER_EXCEPTION,
                        ErrorType.NOT_FOUND_USER_EXCEPTION.getMessage() + memberId));
    Review foundReview =
        reviewRepository
            .findByReviewId(reviewId)
            .orElseThrow(
                () ->
                    new NotFoundException(
                        ErrorType.NOT_FOUND_REVIEW_EXCEPTION,
                        ErrorType.NOT_FOUND_REVIEW_EXCEPTION.getMessage() + reviewId));

    ReviewReport reviewReport =
        ReviewReportMapper.INSTANCE.toReviewReportEntity(request, foundMember, foundReview);

    ReviewReport savedReviewReport = reviewReportRepository.saveAndFlush(reviewReport);
    sendReviewReportMessage(foundReview, savedReviewReport);

    return ReviewReportResponseDTO.builder()
        .reviewReportId(savedReviewReport.getReviewReportId())
        .build();
  }

  private void sendReviewReportMessage(Review review, ReviewReport reviewReport) {
    ReviewReportSlackVO vo =
        ReviewReportMapper.INSTANCE.toReviewReportSlackVO(review, reviewReport);
    slackSender.sendReviewReportMessage(vo);
  }
}
