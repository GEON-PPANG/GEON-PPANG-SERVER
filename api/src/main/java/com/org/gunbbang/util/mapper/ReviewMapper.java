package com.org.gunbbang.util.mapper;

import com.org.gunbbang.BestReviewDTO;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.Review;
import com.org.gunbbang.util.RecommendKeywordPercentage;
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

  @Mapping(source = "recommendKeywordPercentage.deliciousPercent", target = "deliciousPercent")
  @Mapping(source = "recommendKeywordPercentage.specialPercent", target = "specialPercent")
  @Mapping(source = "recommendKeywordPercentage.kindPercent", target = "kindPercent")
  @Mapping(source = "recommendKeywordPercentage.zeroWastePercent", target = "zeroWastePercent")
  ReviewListResponseDTO toReviewListResponseDTO(
      RecommendKeywordPercentage recommendKeywordPercentage,
      long totalReviewCount,
      List<ReviewResponseDTO> reviewList);

  ReviewDetailResponseDTO toReviewDetailResponseDTO(
      Review review, List<RecommendKeywordResponseDTO> recommendKeywordList);
}
