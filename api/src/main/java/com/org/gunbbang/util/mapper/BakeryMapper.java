package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.BakeryListResponseDTO;
import com.org.gunbbang.controller.DTO.response.BakerySearchResponseDTO;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
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
  BakeryListResponseDTO toBakeryListResponseDTO(
      Bakery bakery, boolean isBookMarked, BreadTypeResponseDTO breadType);

  BakerySearchResponseDTO toBakerySearchResponseDTO(
      int resultCount, List<BakeryListResponseDTO> bakeryList);
}
