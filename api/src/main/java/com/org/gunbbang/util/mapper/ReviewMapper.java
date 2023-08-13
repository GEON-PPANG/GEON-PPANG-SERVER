package com.org.gunbbang.util.mapper;

import com.org.gunbbang.BestReviewDTO;
import com.org.gunbbang.controller.DTO.response.BestReviewListResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface ReviewMapper {
  ReviewMapper INSTANCE = Mappers.getMapper(ReviewMapper.class);

  BestReviewListResponseDTO toBestReviewListResponseDTO(
      BestReviewDTO bestReviewDTO,
      String firstMaxRecommendKeyword,
      String secondMaxRecommendKeyword);
}
