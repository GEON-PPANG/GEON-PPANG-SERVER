package com.org.gunbbang.util.mapper;

import com.org.gunbbang.controller.DTO.response.MenuResponseDTO;
import com.org.gunbbang.entity.Menu;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface MenuMapper {
  MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

  List<MenuResponseDTO> toMenuResponseDTOList(List<Menu> menuList);
}
