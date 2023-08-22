package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.request.ReviewReportRequestDTO;
import com.org.gunbbang.entity.Member;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.entity.ReviewReport;
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
}
