package com.org.gunbbang.util.mapper;

import com.org.gunbbang.BestReviewDTO;
import com.org.gunbbang.controller.DTO.response.BestReviewListResponseDTO;
import com.org.gunbbang.controller.DTO.response.RecommendKeywordResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewListResponseDTO;
import com.org.gunbbang.controller.DTO.response.ReviewResponseDTO;
import com.org.gunbbang.entity.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewMapper {
  ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

  BestReviewListResponseDTO toBestReviewListResponseDTO(
      BestReviewDTO bestReviewDTO,
      String firstMaxRecommendKeyword,
      String secondMaxRecommendKeyword);

  @Mapping(target = "memberNickname", expression = "java( review.getMember().getNickname() )")
  @Mapping(source = "createdAt", target = "createdAt")
  ReviewResponseDTO toReviewResponseDTO(
      Review review, String createdAt, List<RecommendKeywordResponseDTO> recommendKeywordList);

  ReviewListResponseDTO toReviewListResponseDTO(
      float deliciousPercent,
      float specialPercent,
      float kindPercent,
      float zeroPercent,
      long totalReviewCount,
      List<ReviewResponseDTO> reviewList);
}
