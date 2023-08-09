package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.entity.BreadType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BreadTypeMapper {
  BreadTypeMapper INSTANCE = Mappers.getMapper(BreadTypeMapper.class);

  BreadTypeResponseDTO toBreadTypeResponseDTO(BreadType breadType);
}
