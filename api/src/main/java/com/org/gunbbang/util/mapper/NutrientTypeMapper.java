package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.NutrientTypeResponseDTO;
import com.org.gunbbang.entity.NutrientType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface NutrientTypeMapper {
  NutrientTypeMapper INSTANCE = Mappers.getMapper(NutrientTypeMapper.class);

  NutrientTypeResponseDTO toNutrientTypeResponseDTO(NutrientType nutrientType);
}
