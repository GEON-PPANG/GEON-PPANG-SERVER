package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.RecommendKeywordResponseDTO;
import com.org.gunbbang.entity.ReviewRecommendKeyword;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RecommendKeywordMapper {
  RecommendKeywordMapper INSTANCE = Mappers.getMapper(RecommendKeywordMapper.class);

  @Mapping(source = "recommendKeyword.recommendKeywordId", target = "recommendKeywordId")
  @Mapping(source = "recommendKeyword.keywordName", target = "recommendKeywordName")
  RecommendKeywordResponseDTO toRecommendKeywordResponseDTO(
      ReviewRecommendKeyword reviewRecommendKeyword);

  @IterableMapping(elementTargetType = RecommendKeywordResponseDTO.class)
  List<RecommendKeywordResponseDTO> toRecommendKeywordListResponseDTO(
      List<ReviewRecommendKeyword> recommendKeywords);
}
