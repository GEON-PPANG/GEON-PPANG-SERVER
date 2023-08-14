package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.entity.Bakery;
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
  BakeryListResponseDTO toBakeryListResponseDTO(Bakery bakery, BreadTypeResponseDTO breadType);

  BakerySearchResponseDTO toBakerySearchResponseDTO(
      int resultCount, List<BakeryListResponseDTO> bakeryList);

  List<BestBakeryListResponseDTO> toBestBakeryListResponseDTO(List<Bakery> bakeries);

  @Mapping(source = "bakery.isHACCP", target = "isHACCP")
  @Mapping(source = "bakery.isVegan", target = "isVegan")
  @Mapping(source = "bakery.isNonGMO", target = "isNonGMO")
  BakeryDetailResponseDTO toBakeryDetailResponseDTO(
      Bakery bakery,
      String address,
      BreadTypeResponseDTO breadType,
      boolean isBookMarked,
      List<MenuResponseDTO> menuList);
}
