package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.entity.MemberBreadType;
import java.util.List;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberBreadTypeMapper {
  MemberBreadTypeMapper INSTANCE = Mappers.getMapper(MemberBreadTypeMapper.class);

  @Mapping(target = "breadTypeId", source = "breadType.breadTypeId")
  BreadTypeResponseDTO toBreadTypeResponseDTO(MemberBreadType memberBreadType);

  @IterableMapping(elementTargetType = BreadTypeResponseDTO.class)
  List<BreadTypeResponseDTO> toBreadTypeResponseDTOList(List<MemberBreadType> memberBreadTypeList);
}
