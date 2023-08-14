package com.org.gunbbang.service;

import com.org.gunbbang.CategoryType;
import com.org.gunbbang.NotFoundException;
import com.org.gunbbang.controller.DTO.response.*;
import com.org.gunbbang.controller.DTO.response.BaseDTO.BaseBakeryResponseDTOV2;
import com.org.gunbbang.entity.*;
import com.org.gunbbang.errorType.ErrorType;
import com.org.gunbbang.repository.*;
import com.org.gunbbang.service.specification.BakerySpecifications;
import com.org.gunbbang.util.mapper.BakeryMapper;
import com.org.gunbbang.util.mapper.BreadTypeMapper;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BakeryService {
  private final CategoryRepository categoryRepository;
  private final BookMarkRepository bookMarkRepository;
  private final MemberRepository memberRepository;
  private final BakeryRepository bakeryRepository;
  private final MenuRepository menuRepository;

  private final int maxBestBakeryCount = 10;

  public List<BakeryListResponseDTO> getBakeryList(
      Long memberId, String sort, boolean isHard, boolean isDessert, boolean isBrunch) {
    List<Category> categoryList = getCategoryList(isHard, isDessert, isBrunch);
    List<BakeryListResponseDTO> responseDtoList = new ArrayList<>();
    BreadTypeResponseDTO breadType;
    List<Bakery> bakeryList;

    if (categoryList.isEmpty()) {
      Sort sortOption =
          sort.equals("review")
              ? Sort.by(Sort.Direction.DESC, "reviewCount")
              : Sort.by(Sort.Direction.DESC, "bakeryId");
      bakeryList = bakeryRepository.findAll(sortOption);
      for (Bakery bakery : bakeryList) {
        breadType = getBreadType(bakery);
        BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(bakery, breadType);
        responseDtoList.add(bakeryListResponseDto);
      }
      return responseDtoList;
    }

    if (sort.equals("review")) {
      bakeryList = bakeryRepository.findBakeriesByCategoryAndReview(categoryList);
      for (Bakery bakery : bakeryList) {
        breadType = getBreadType(bakery);
        BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(bakery, breadType);
        responseDtoList.add(bakeryListResponseDto);
      }
      return responseDtoList;
    }

    bakeryList = bakeryRepository.findBakeriesByCategory(categoryList);
    for (Bakery bakery : bakeryList) {
      breadType = getBreadType(bakery);
      BakeryListResponseDTO bakeryListResponseDto = getBakeryResponseDTO(bakery, breadType);
      responseDtoList.add(bakeryListResponseDto);
    }
    return responseDtoList;
  }

  public BakeryDetailResponseDTO getBakeryDetail(Long memberId, Long bakeryId) {
    Bakery bakery =
        bakeryRepository
            .findById(bakeryId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_BAKERY_EXCEPTION));
    List<Menu> bakeryMenu = menuRepository.findAllByBakery(bakery);
    BreadTypeResponseDTO breadType = getBreadType(bakery);
    boolean isBookMarked = isBookMarked(memberId, bakeryId);
    List<MenuResponseDTO> menuList = new ArrayList<>();

    for (Menu menu : bakeryMenu) {
      menuList.add(
          MenuResponseDTO.builder()
              .menuId(menu.getMenuId())
              .menuName(menu.getMenuName())
              .menuPrice(menu.getMenuPrice())
              .build());
    }

    return BakeryDetailResponseDTO.builder()
        .bakeryId(bakery.getBakeryId())
        .bakeryName(bakery.getBakeryName())
        .bakeryPicture(bakery.getBakeryPicture())
        .isHACCP(bakery.getIsHACCP())
        .isVegan(bakery.getIsVegan())
        .isNonGMO(bakery.getIsNonGMO())
        .breadType(breadType)
        .firstNearStation(bakery.getFirstNearStation())
        .secondNearStation(bakery.getSecondNearStation())
        .isBookMarked(isBookMarked)
        .bookMarkCount(bakery.getBookMarkCount())
        .reviewCount(bakery.getReviewCount())
        .mapUrl(bakery.getMapUrl())
        .homepageUrl(bakery.getHomepageUrl())
        .instagramUrl(bakery.getInstagramUrl())
        .address(
            bakery.getState()
                + " "
                + bakery.getCity()
                + " "
                + bakery.getTown()
                + " "
                + bakery.getAddressRest())
        .openingTime(bakery.getOpeningHours())
        .closedDay(bakery.getClosedDay())
        .phoneNumber(bakery.getPhoneNumber())
        .menuList(menuList)
        .build();
  }

  public List<BestBakeryListResponseDTO> getBestBakeries(Long memberId) {
    List<Long> alreadyFoundBakeryIds = new ArrayList<>();
    alreadyFoundBakeryIds.add(-1L);

    Member foundMember =
        memberRepository
            .findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_USER_EXCEPTION));

    List<Bakery> bestBakeries = getBestBakeries(foundMember);

    if (bestBakeries.size() == maxBestBakeryCount) {
      log.info("베스트 베이커리 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
    }

    setAlreadyFoundBakeryIds(alreadyFoundBakeryIds, bestBakeries);
    getRestBakeries(alreadyFoundBakeryIds, foundMember.getBreadType(), bestBakeries);

    if (bestBakeries.size() == maxBestBakeryCount) {
      log.info("빵유형 10개 조회 완료. 추가 조회 쿼리 없이 바로 반환");
      return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
    }

    setAlreadyFoundBakeryIds(alreadyFoundBakeryIds, bestBakeries);
    getRandomBakeries(alreadyFoundBakeryIds, bestBakeries);

    return BakeryMapper.INSTANCE.toBestBakeryListResponseDTO(bestBakeries);
  }

  private void setAlreadyFoundBakeryIds(
      List<Long> alreadyFoundBakeryIds, List<Bakery> bestBakeries) {
    alreadyFoundBakeryIds.addAll(
        bestBakeries.stream().map(Bakery::getBakeryId).collect(Collectors.toList()));
  }

  private void getRandomBakeries(List<Long> alreadyFoundBakeryIds, List<Bakery> bestBakeries) {
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
    log.info("나머지만 랜덤으로 고르는 베이커리. 현재까지 조회된 베이커리 수: " + bestBakeries.size());
    bestBakeries.addAll(
        bakeryRepository.findRestBakeriesRandomly(alreadyFoundBakeryIds, bestPageRequest));
  }

  private void getRestBakeries(
      List<Long> alreadyFoundBakeryIds, BreadType breadType, List<Bakery> bestBakeries) {
    log.info("빵유형 일치 베이커리 조회 시작. 현재까지 조회된 베이커리 수: " + bestBakeries.size());
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount - bestBakeries.size());
    bestBakeries.addAll(
        bakeryRepository.findRestBakeriesByBreadTypeId(
            breadType, alreadyFoundBakeryIds, bestPageRequest));
  }

  private List<Bakery> getBestBakeries(Member foundMember) {
    log.info("베스트 건빵집 조회 시작.");
    PageRequest bestPageRequest = PageRequest.of(0, maxBestBakeryCount);
    List<Bakery> bestBakeries =
        bakeryRepository.findBestBakeries(
            foundMember.getBreadType().getBreadTypeId(),
            foundMember.getMainPurpose(),
            bestPageRequest);
    return bestBakeries;
  }

  //  public BakerySearchResponseDTO getBakeriesByName(String bakeryName, Long memberId) {
  //    if (bakeryName.isEmpty()) {
  //      return BakerySearchResponseDTO.getEmptyBakerySearchResponseDTO();
  //    }
  //
  //    List<Bakery> foundBakeries = bakeryRepository.findBakeryByBakeryName(bakeryName);
  //    List<BakeryListResponseDTO> bakeryListResponseDTOs =
  //        getBakeryListResponseDTOList(foundBakeries);
  //
  //    return BakeryMapper.INSTANCE.toBakerySearchResponseDTO(
  //        bakeryListResponseDTOs.size(), bakeryListResponseDTOs);
  //  }

  public BakerySearchResponseDTO getBakeriesBySearch(String searchTerm) {
    if (searchTerm.isEmpty()) {
      return BakerySearchResponseDTO.getEmptyBakerySearchResponseDTO();
    }
    List<String> searchWordList = new ArrayList<>(Arrays.asList(searchTerm.split("\\s+")));

    Specification<Bakery> spec = Specification.where(null);

    for (String keyword : searchWordList) {
      Specification<Bakery> keywordSpec = BakerySpecifications.searchBakery(keyword);
      spec = spec.and(keywordSpec);
    }

    List<Bakery> foundBakeries = bakeryRepository.findAll(spec);
    List<BakeryListResponseDTO> bakeryListResponseDTOs =
        getBakeryListResponseDTOList(foundBakeries);

    return BakeryMapper.INSTANCE.toBakerySearchResponseDTO(
        bakeryListResponseDTOs.size(), bakeryListResponseDTOs);
  }

  private List<BakeryListResponseDTO> getBakeryListResponseDTOList(List<Bakery> foundBakeries) {
    List<BakeryListResponseDTO> bakeryListResponseDTOs = new ArrayList<>();

    for (Bakery foundBakery : foundBakeries) {
      BreadTypeResponseDTO breadType =
          BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(foundBakery.getBreadType());
      BakeryListResponseDTO bakeryListResponseDTO =
          BakeryMapper.INSTANCE.toBakeryListResponseDTO(foundBakery, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTO);
    }

    return bakeryListResponseDTOs;
  }

  public BakerySearchResponseDTOV2 getBakeriesByNameV2(String bakeryName, Long memberId) {
    if (bakeryName.isEmpty()) {
      return BakerySearchResponseDTOV2.getEmptyBakerySearchResponseDTO();
    }

    List<Bakery> foundBakeries = bakeryRepository.findBakeryByBakeryName(bakeryName);
    List<BakeryListResponseDTOV2> bakeryListResponseDTOs = new ArrayList<>();
    for (Bakery foundBakery : foundBakeries) {
      boolean isBookMarked = isBookMarked(memberId, foundBakery.getBakeryId());
      BreadTypeResponseDTO breadType =
          BreadTypeMapper.INSTANCE.toBreadTypeResponseDTO(foundBakery.getBreadType());
      BakeryListResponseDTOV2 bakeryListResponseDTOV2 =
          getBakeryResponseDTOV2(foundBakery, isBookMarked, breadType);
      bakeryListResponseDTOs.add(bakeryListResponseDTOV2);
    }

    return BakerySearchResponseDTOV2.builder()
        .resultCount(foundBakeries.size())
        .bakeryList(bakeryListResponseDTOs)
        .build();
  }

  public List<BakeryListResponseDTO> getBookMarkedBakeries(Long memberId) {
    List<Bakery> bookMarkedBakeries = bakeryRepository.findBookMarkedBakeries(memberId);
    return getBakeryListResponseDTOList(bookMarkedBakeries);
  }

  // TODO: 공통적으로 사용되는 메서드라 다른 곳으로는 못뺄지
  private boolean isBookMarked(Long memberId, Long bakeryId) {
    if (bookMarkRepository.findByMemberIdAndBakeryId(memberId, bakeryId).isPresent()) {
      return true;
    }
    return false;
  }

  private BreadTypeResponseDTO getBreadType(Bakery bakery) {
    return BreadTypeResponseDTO.builder()
        .breadTypeId(bakery.getBreadType().getBreadTypeId())
        .breadTypeName(bakery.getBreadType().getBreadTypeName())
        .isGlutenFree(bakery.getBreadType().getIsGlutenFree())
        .isVegan(bakery.getBreadType().getIsVegan())
        .isNutFree(bakery.getBreadType().getIsNutFree())
        .isSugarFree(bakery.getBreadType().getIsSugarFree())
        .build();
  }

  private BakeryListResponseDTO getBakeryResponseDTO(
      Bakery bakery, BreadTypeResponseDTO breadType) {
    return BakeryListResponseDTO.builder()
        .bakeryId(bakery.getBakeryId())
        .bakeryName(bakery.getBakeryName())
        .bakeryPicture(bakery.getBakeryPicture())
        .isHACCP(bakery.getIsHACCP())
        .isVegan(bakery.getIsVegan())
        .isNonGMO(bakery.getIsNonGMO())
        .firstNearStation(bakery.getFirstNearStation())
        .secondNearStation(bakery.getSecondNearStation())
        .bookMarkCount(bakery.getBookMarkCount())
        .reviewCount(bakery.getReviewCount())
        .breadType(breadType)
        .build();
  }

  private BakeryListResponseDTOV2 getBakeryResponseDTOV2(
      Bakery bakery, boolean isBookMarked, BreadTypeResponseDTO breadType) {
    BaseBakeryResponseDTOV2 baseBakeryResponseDTOV2 =
        BaseBakeryResponseDTOV2.builder()
            .bakeryId(bakery.getBakeryId())
            .bakeryName(bakery.getBakeryName())
            .bakeryPicture(bakery.getBakeryPicture())
            .isHACCP(bakery.getIsHACCP())
            .isVegan(bakery.getIsVegan())
            .isNonGMO(bakery.getIsNonGMO())
            .firstNearStation(bakery.getFirstNearStation())
            .secondNearStation(bakery.getSecondNearStation())
            .build();

    return BakeryListResponseDTOV2.builder()
        .baseBakeryResponseDTOV2(baseBakeryResponseDTOV2)
        .breadType(breadType)
        .reviewCount(bakery.getReviewCount())
        .isBookMarked(isBookMarked)
        .build();
  }

  private List<Category> getCategoryList(boolean isHard, boolean isDessert, boolean isBrunch) {
    List<Category> categoryList = new ArrayList<>();
    if (isHard) {
      categoryList.add(
          categoryRepository
              .findByCategoryName(CategoryType.HARD_BREAD.getName())
              .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
    }
    if (isDessert) {
      categoryList.add(
          categoryRepository
              .findByCategoryName(CategoryType.DESSERT.getName())
              .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
    }
    if (isBrunch) {
      categoryList.add(
          categoryRepository
              .findByCategoryName(CategoryType.BRUNCH.getName())
              .orElseThrow(() -> new NotFoundException(ErrorType.NOT_FOUND_CATEGORY_EXCEPTION)));
    }
    return categoryList;
  }
}
