package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.entity.BakeryBreadType;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BakeryBreadTypeMapper {
  BakeryBreadTypeMapper INSTANCE = Mappers.getMapper(BakeryBreadTypeMapper.class);

  @Mapping(target = "breadTypeId", source = "breadType.breadTypeId")
  BreadTypeResponseDTO toBreadTypeResponseDTO(BakeryBreadType bakeryBreadType);

  @IterableMapping(elementTargetType = BreadTypeResponseDTO.class)
  List<BreadTypeResponseDTO> toBreadTypeResponseDTOList(List<BakeryBreadType> bakeryBreadTypeList);
}
