package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.request.ReviewReportRequestDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.entity.ReviewReport;
import com.org.gunbbang.service.vo.ReviewReportSlackVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface ReviewReportMapper {
  ReviewReportMapper INSTANCE = Mappers.getMapper(ReviewReportMapper.class);

  @Mapping(source = "member", target = "member")
  @Mapping(source = "review", target = "review")
  ReviewReport toReviewReportEntity(ReviewReportRequestDTO request, Member member, Review review);

  /*
  private final String bakeryName; // review에 있음
    private final Long reporterId; // ReviewReport 있음
    private final Long reviewReportId; // ReviewReport 있음 이미됨
    private final ReportCategory reportCategory; // ReviewReport 있음 이미됨
    private final String reportContent; // ReviewReport 있음
    private final String reviewContent; // review에 있음
    private final LocalDateTime reportedAt; // ReviewReport에 있음
   */

  @Mapping(source = "review.bakery.bakeryName", target = "bakeryName")
  @Mapping(source = "reviewReport.member.memberId", target = "reporterId")
  @Mapping(source = "reviewReport.content", target = "reportContent")
  @Mapping(source = "review.reviewText", target = "reviewContent")
  @Mapping(source = "reviewReport.createdAt", target = "reportedAt")
  ReviewReportSlackVO toReviewReportSlackVO(Review review, ReviewReport reviewReport);
}
