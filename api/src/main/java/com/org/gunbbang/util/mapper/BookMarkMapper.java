package com.org.gunbbang.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface BookMarkMapper {
  BookMarkMapper INSTANCE = Mappers.getMapper(BookMarkMapper.class);
}
