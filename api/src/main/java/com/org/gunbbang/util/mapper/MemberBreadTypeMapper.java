package com.org.gunbbang.util.mapper;

import com.org.gunbbang.BreadTypeTag;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.entity.MemberBreadType;
import com.org.gunbbang.util.mapper.context.CycleAvoidingMappingContext;
import java.util.List;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MemberBreadTypeMapper {
  MemberBreadTypeMapper INSTANCE = Mappers.getMapper(MemberBreadTypeMapper.class);

  @Mapping(target = "isGlutenFree", expression = "java(mapIsGlutenFree(memberBreadTypes))")
  @Mapping(target = "isVegan", expression = "java(mapIsVegan(memberBreadTypes))")
  @Mapping(target = "isNutFree", expression = "java(mapIsNutFree(memberBreadTypes))")
  @Mapping(target = "isSugarFree", expression = "java(mapIsSugarFree(memberBreadTypes))")
  BreadTypeResponseDTO toBreadTypeResponseDTO(
      List<MemberBreadType> memberBreadTypes, @Context CycleAvoidingMappingContext context);

  default boolean mapIsGlutenFree(List<MemberBreadType> memberBreadTypes) {
    return memberBreadTypes.stream()
        .anyMatch(
            memberBreadType ->
                memberBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.GLUTEN_FREE);
  }

  default boolean mapIsVegan(List<MemberBreadType> memberBreadTypes) {
    return memberBreadTypes.stream()
        .anyMatch(
            memberBreadType ->
                memberBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.VEGAN);
  }

  default boolean mapIsNutFree(List<MemberBreadType> memberBreadTypes) {
    return memberBreadTypes.stream()
        .anyMatch(
            memberBreadType ->
                memberBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.NUT_FREE);
  }

  default boolean mapIsSugarFree(List<MemberBreadType> memberBreadTypes) {
    return memberBreadTypes.stream()
        .anyMatch(
            memberBreadType ->
                memberBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.SUGAR_FREE);
  }

  /*
  @Mapping(target = "isGlutenFree", expression = "java(mapIsGlutenFree(memberBreadType))")
  @Mapping(target = "isVegan", expression = "java(mapIsVegan(memberBreadType))")
  @Mapping(target = "isNutFree", expression = "java(mapIsNutFree(memberBreadType))")
  @Mapping(target = "isSugarFree", expression = "java(mapIsSugarFree(memberBreadType))")
  @Mapping(source = "memberBreadType.memberBreadTypeId", target = "breadTypeId")
  BreadTypeResponseDTO toBreadTypeResponseDTO(MemberBreadType memberBreadType);

  default boolean mapIsGlutenFree(MemberBreadType memberBreadType) {
      return BreadTypeTag.GLUTEN_FREE.equals(memberBreadType.getBreadType().getBreadTypeTag());
  }

  default boolean mapIsVegan(MemberBreadType memberBreadType) {
      return BreadTypeTag.GLUTEN_FREE.equals(memberBreadType.getBreadType().getBreadTypeTag());
  }

  default boolean mapIsNutFree(MemberBreadType memberBreadType) {
      return BreadTypeTag.GLUTEN_FREE.equals(memberBreadType.getBreadType().getBreadTypeTag());
  }

  default boolean mapIsSugarFree(MemberBreadType memberBreadType) {
      return BreadTypeTag.SUGAR_FREE.equals(memberBreadType.getBreadType().getBreadTypeTag());
  }
   */

}
