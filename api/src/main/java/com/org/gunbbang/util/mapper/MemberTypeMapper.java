package com.org.gunbbang.util.mapper;

import com.org.gunbbang.MainPurpose;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberDetailResponseDTO;
import com.org.gunbbang.controller.DTO.response.MemberTypeResponseDTO;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberTypeMapper {
  MemberTypeMapper INSTANCE = Mappers.getMapper(MemberTypeMapper.class);

  @Mapping(source = "breadTypeList", target = "breadTypeList")
  @Mapping(source = "nutrientTypeList", target = "nutrientTypeList")
  MemberTypeResponseDTO toMemberTypeResponseDTO(
      Long memberId,
      MainPurpose mainPurpose,
      String nickname,
      List<Long> breadTypeList,
      List<Long> nutrientTypeList);

  MemberDetailResponseDTO toMemberDetailResponseDTO(
      String memberNickname, MainPurpose mainPurpose, List<BreadTypeResponseDTO> breadType);
}
