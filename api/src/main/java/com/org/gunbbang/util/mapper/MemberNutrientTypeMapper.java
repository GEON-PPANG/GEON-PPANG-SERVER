package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.NutrientTypeResponseDTO;
import com.org.gunbbang.entity.MemberNutrientType;
import java.util.List;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberNutrientTypeMapper {
  MemberNutrientTypeMapper INSTANCE = Mappers.getMapper(MemberNutrientTypeMapper.class);

  @Mapping(target = "nutrientTypeId", source = "nutrientType.nutrientTypeId")
  NutrientTypeResponseDTO toNutrientTypeResponseDTO(MemberNutrientType memberNutrientType);

  @IterableMapping(elementTargetType = NutrientTypeResponseDTO.class)
  List<NutrientTypeResponseDTO> toNutrientTypeResponseDTOList(
      List<MemberNutrientType> memberNutrientTypes);
}
