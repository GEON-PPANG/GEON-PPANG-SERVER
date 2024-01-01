package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.Bakery;
import com.org.gunbbang.entity.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BakeryMapper {
  BakeryMapper INSTANCE = Mappers.getMapper(BakeryMapper.class);

  @Mapping(source = "bakery.isHACCP", target = "isHACCP")
  @Mapping(source = "bakery.isVegan", target = "isVegan")
  @Mapping(source = "bakery.isNonGMO", target = "isNonGMO")
  BakeryListResponseDTO toBakeryListResponseDTO(
      Bakery bakery, List<BreadTypeResponseDTO> breadTypeList);

  BakerySearchResponseDTO toBakerySearchResponseDTO(
      int resultCount, List<BakeryListResponseDTO> bakeryList);

  List<BestBakeryListResponseDTO> toBestBakeryListResponseDTO(List<Bakery> bakeries);

  @Mapping(source = "bakery.isHACCP", target = "isHACCP")
  @Mapping(source = "bakery.isVegan", target = "isVegan")
  @Mapping(source = "bakery.isNonGMO", target = "isNonGMO")
  @Mapping(source = "breadTypeList", target = "breadType")
  BakeryDetailResponseDTO toBakeryDetailResponseDTO(
      Bakery bakery,
      String address,
      List<BreadTypeResponseDTO> breadTypeList,
      boolean isBookMarked,
      List<MenuResponseDTO> menuList);

  @Mapping(source = "bakery.isHACCP", target = "isHACCP")
  @Mapping(source = "bakery.isVegan", target = "isVegan")
  @Mapping(source = "bakery.isNonGMO", target = "isNonGMO")
  @Mapping(source = "breadTypeList", target = "breadTypeList")
  @Mapping(source = "review.reviewId", target = "reviewId")
  @Mapping(source = "review.createdAt", target = "createdAt", dateFormat = "yy.MM.dd")
  BakeryListReviewedByMemberDTO toListReviewedByMemberDTO(
      Bakery bakery, Review review, List<BreadTypeResponseDTO> breadTypeList);
}
