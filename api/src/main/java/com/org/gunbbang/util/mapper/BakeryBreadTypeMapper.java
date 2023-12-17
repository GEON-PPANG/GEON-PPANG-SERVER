package com.org.gunbbang.util.mapper;

import com.org.gunbbang.BreadTypeTag;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.entity.BakeryBreadType;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BakeryBreadTypeMapper {
  BakeryBreadTypeMapper INSTANCE = Mappers.getMapper(BakeryBreadTypeMapper.class);

  @Mapping(target = "isGlutenFree", expression = "java(mapIsGlutenFree(bakeryBreadTypes))")
  @Mapping(target = "isVegan", expression = "java(mapIsVegan(bakeryBreadTypes))")
  @Mapping(target = "isNutFree", expression = "java(mapIsNutFree(bakeryBreadTypes))")
  @Mapping(target = "isSugarFree", expression = "java(mapIsSugarFree(bakeryBreadTypes))")
  BreadTypeResponseDTO toBreadTypeResponseDTO(List<BakeryBreadType> bakeryBreadTypes);

  default boolean mapIsGlutenFree(List<BakeryBreadType> bakeryBreadTypes) {
    return bakeryBreadTypes.stream()
        .anyMatch(
            bakeryBreadType ->
                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.GLUTEN_FREE);
  }

  default boolean mapIsVegan(List<BakeryBreadType> bakeryBreadTypes) {
    return bakeryBreadTypes.stream()
        .anyMatch(
            bakeryBreadType ->
                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.VEGAN);
  }

  default boolean mapIsNutFree(List<BakeryBreadType> bakeryBreadTypes) {
    return bakeryBreadTypes.stream()
        .anyMatch(
            bakeryBreadType ->
                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.NUT_FREE);
  }

  default boolean mapIsSugarFree(List<BakeryBreadType> bakeryBreadTypes) {
    return bakeryBreadTypes.stream()
        .anyMatch(
            bakeryBreadType ->
                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.SUGAR_FREE);
  }
}
