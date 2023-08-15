package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.RecommendKeywordResponseDTO;
import com.org.gunbbang.entity.ReviewRecommendKeyword;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RecommendKeywordMapper {
  RecommendKeywordMapper INSTANCE = Mappers.getMapper(RecommendKeywordMapper.class);

  @Mapping(
      source = "reviewRecommendKeyword.recommendKeyword.recommendKeywordId",
      target = "recommendKeywordId")
  @Mapping(
      source = "reviewRecommendKeyword.recommendKeyword.keywordName",
      target = "recommendKeywordName")
  RecommendKeywordResponseDTO toRecommendKeywordResponseDTO(
      ReviewRecommendKeyword reviewRecommendKeyword);
}
