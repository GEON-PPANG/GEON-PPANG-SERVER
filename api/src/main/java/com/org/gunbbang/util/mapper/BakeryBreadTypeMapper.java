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
  @Mapping(target = "breadTypeTag", source = "breadType.breadTypeTag")
  BreadTypeResponseDTO toBreadTypeResponseDTO(BakeryBreadType bakeryBreadType);

  @IterableMapping(elementTargetType = BreadTypeResponseDTO.class)
  List<BreadTypeResponseDTO> toBreadTypeResponseDTOList(List<BakeryBreadType> bakeryBreadTypeList);
}

  //  default boolean mapIsGlutenFree(List<BakeryBreadType> bakeryBreadTypes) {
  //    return bakeryBreadTypes.stream()
  //        .anyMatch(
  //            bakeryBreadType ->
  //                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.GLUTEN_FREE);
  //  }
  //
  //  default boolean mapIsVegan(List<BakeryBreadType> bakeryBreadTypes) {
  //    return bakeryBreadTypes.stream()
  //        .anyMatch(
  //            bakeryBreadType ->
  //                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.VEGAN);
  //  }
  //
  //  default boolean mapIsNutFree(List<BakeryBreadType> bakeryBreadTypes) {
  //    return bakeryBreadTypes.stream()
  //        .anyMatch(
  //            bakeryBreadType ->
  //                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.NUT_FREE);
  //  }
  //
  //  default boolean mapIsSugarFree(List<BakeryBreadType> bakeryBreadTypes) {
  //    return bakeryBreadTypes.stream()
  //        .anyMatch(
  //            bakeryBreadType ->
  //                bakeryBreadType.getBreadType().getBreadTypeTag() == BreadTypeTag.SUGAR_FREE);
  //  }
// }
