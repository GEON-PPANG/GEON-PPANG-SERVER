package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.MemberSignUpResponseDTO;
import com.org.gunbbang.service.VO.SignedUpMemberVO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface MemberMapper {
  MemberMapper INSTANCE = Mappers.getMapper(MemberMapper.class);

  //  Member toMemberEntity(
  //      MemberSignUpRequestDTO request, BreadType breadType, NutrientType nutrientType, Role
  // role);

  //  SignedUpMemberVO toSignedUpMemberVO(
  //      MemberSignUpRequestDTO request, BreadType breadType, NutrientType nutrientType, Role role,
  // String appleRefreshToken);

  MemberSignUpResponseDTO toMemberSignUpResponseDTO(SignedUpMemberVO vo);
}
