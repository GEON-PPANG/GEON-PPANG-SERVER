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

  @Mapping(
      target = "createdAt",
      expression = "java( review.getCreatedAt().format(DateTimeFormatter.ofPattern(\"yy.MM.dd\") )")
  ReviewResponseDTO toReviewResponseDTO(
      Review review, List<RecommendKeywordResponseDTO> recommendKeywordResponseDTOList);

  ReviewListResponseDTO toReviewListResponseDTO(
      float tastePercent,
      float specialPercent,
      float kindPercent,
      float zeroPercent,
      int totalReviewCount,
      List<ReviewResponseDTO> reviewResponseDTOList);
}
