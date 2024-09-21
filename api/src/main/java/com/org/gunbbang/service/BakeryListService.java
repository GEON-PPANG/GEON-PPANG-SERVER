package com.org.gunbbang.service;

import static com.org.gunbbang.CategoryType.getCategoryTypeList;
import static com.org.gunbbang.support.errorType.ErrorType.REQUEST_VALIDATION_EXCEPTION;

import com.org.gunbbang.support.exception.BadRequestException;
import com.org.gunbbang.CategoryType;
import com.org.gunbbang.support.exception.NotFoundException;
import com.org.gunbbang.auth.security.util.SecurityUtil;
import com.org.gunbbang.controller.DTO.response.BakeryListResponseDTO;
import com.org.gunbbang.controller.DTO.response.BreadTypeResponseDTO;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.support.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.util.mapper.BakeryBreadTypeMapper;
import com.org.gunbbang.util.mapper.BakeryMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryListService {
  private final CategoryRepository categoryRepository;
  private final MemberNutrientTypeRepository memberNutrientTypeRepository;
  private final NutrientTypeRepository nutrientTypeRepository;
  private final MemberBreadTypeRepository memberBreadTypeRepository;
  private final BreadTypeRepository breadTypeRepository;
  private final BakeryRepository bakeryRepository;
  private final BakeryBreadTypeRepository bakeryBreadTypeRepository;

  public Page<BakeryListResponseDTO> getBakeryList(
      String sortingOption,
      boolean personalFilter,
      boolean isHard,
      boolean isDessert,
      boolean isBrunch,
      PageRequest pageRequest) {
    final List<Category> categoryList = getCategoryList(isHard, isDessert, isBrunch);
    final List<NutrientType> nutrientTypeList = getNutrientTypeList(personalFilter);
    final List<BreadType> breadTypeList = getBreadTypeList(personalFilter);
    if (sortingOption.equals("review")) {
      System.out.println("hello======================");
      final Page<Bakery> bakeryList =
          bakeryRepository.findFilteredBakeriesSortByReview(
              categoryList, breadTypeList, nutrientTypeList, pageRequest);
      return getBakeryListResponseDTOList(bakeryList);
    }
    final Page<Bakery> bakeryList =
        bakeryRepository.findFilteredBakeries(
            categoryList, breadTypeList, nutrientTypeList, pageRequest);
    return getBakeryListResponseDTOList(bakeryList);
  }

  private List<BreadType> getBreadTypeList(boolean personalFilter) {
    if (!SecurityUtil.checkAnonymousUser() && personalFilter)
      return memberBreadTypeRepository.findAllByMemberId(SecurityUtil.getLoginMemberId()).stream()
          .map(MemberBreadType::getBreadType)
          .collect(Collectors.toList());
    else if (personalFilter) {
      throw new BadRequestException(REQUEST_VALIDATION_EXCEPTION);
    } else return breadTypeRepository.findAll();
  }

  private Page<BakeryListResponseDTO> getBakeryListResponseDTOList(Page<Bakery> foundBakeries) {
    List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();

    for (Bakery foundBakery : foundBakeries) {
      List<BreadTypeResponseDTO> breadType =
          BakeryBreadTypeMapper.INSTANCE.toBreadTypeResponseDTOList(
              getBakeryBreadTypeList(foundBakery));
      BakeryListResponseDTO bakeryListResponseDTO =
          BakeryMapper.INSTANCE.toBakeryListResponseDTO(foundBakery, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTO);
    }

    return new PageImpl<>(
        bakeryListResponseDTOs, foundBakeries.getPageable(), foundBakeries.getTotalElements());
  }

  private List<BakeryBreadType> getBakeryBreadTypeList(Bakery bakery) {
    return bakeryBreadTypeRepository.findAllByBakery(bakery);
  }

  private List<NutrientType> getNutrientTypeList(boolean personalFilter) {
    if (!SecurityUtil.checkAnonymousUser() && personalFilter)
      return memberNutrientTypeRepository.findByMemberId(SecurityUtil.getLoginMemberId()).stream()
          .map(MemberNutrientType::getNutrientType)
          .collect(Collectors.toList());
    else if (personalFilter) {
      throw new BadRequestException(REQUEST_VALIDATION_EXCEPTION);
    } else return nutrientTypeRepository.findAll();
  }

  private List<Category> getCategoryList(boolean isHard, boolean isDessert, boolean isBrunch) {
    List<CategoryType> categoryTypeList = getCategoryTypeList(isHard, isDessert, isBrunch);
    List<Category> categoryList = new ArrayList<>();
    if (categoryTypeList.isEmpty()) {
      return categoryRepository.findAll();
    }
    for (CategoryType categoryType : categoryTypeList) {
      Category category =
          categoryRepository
              .findByCategoryName(categoryType.getName())
              .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION));
      categoryList.add(category);
    }
    return categoryList;
  }
}
